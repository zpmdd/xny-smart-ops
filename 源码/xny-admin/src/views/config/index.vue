<script setup>
import { ref, onMounted, watchEffect, nextTick } from "vue";
import {
  selectList,
  pageDataChart,
  pageDataCount,
  exportDataChart,
  exportDataCount,
  updateDataCount,
} from "@/api/system";
import { exportData } from "@/utils/export";

const activeProject = ref("");
const activeadtaType = ref("");
const resultType = ref("");
const dataSelectList = ref([]);
const dataSelectValue = ref("");

const tableLoading = ref(false);

const queryForm = ref({
  pageNum: 1,
  pageSize: 10,
  projectType: "FBSGF",
  startTime: null,
  endTime: null,
});

const activeForm = ref({});

const daterangeVal = ref(null);

const project = ref([
  {
    name: "分布式光伏",
    value: "FBSGF",
  },
  {
    name: "集中式光伏",
    value: "JZSGF",
  },
]);

const dataType = ref([
  {
    name: "数据统计",
    value: "1",
  },
  {
    name: "图表",
    value: "2",
  },
]);

const tableData = ref([]);
const total = ref(0);
const tableType = ref(1);

const tableRow = ref("");
const tableColumn = ref(-1);

activeProject.value = project.value[0].value;
activeadtaType.value = dataType.value[0].value;

const watchTime = watchEffect(() => {
  if (daterangeVal.value?.length) {
    queryForm.value.startTime = daterangeVal.value[0];
    queryForm.value.endTime = daterangeVal.value[1];
  } else {
    queryForm.value.startTime = null;
    queryForm.value.endTime = null;
  }
});

const watchProject = watchEffect(() => {
  if (activeProject.value == "FBSGF") {
    dataType.value = [
      {
        name: "数据统计",
        value: "1",
      },
    ];
  }
  if (activeProject.value == "JZSGF") {
    dataType.value = [
      {
        name: "数据统计",
        value: "1",
      },
      {
        name: "图表",
        value: "2",
      },
    ];
  }
  if (activeadtaType.value == "1") {
    const config = { projectType: activeProject.value };
    selectList(config).then((res) => {
      let data = res.data;
      dataSelectList.value = data || [];
    });
  }
  nextTick(() => {
    handleQuery();
  });
});

onMounted(() => {
  handleQuery();
});

const handleCheckProject = (item) => {
  activeProject.value = item.value;
  queryForm.value.projectType = item.value;
  activeadtaType.value = dataType.value[0].value;
};

const handleCheckData = (item) => {
  activeadtaType.value = item.value;
};

const handleSelectChange = (value) => {
  dataSelectValue.value = value;
};

const handleExport = () => {
  let config = activeForm.value;
  if (resultType.value == "1") {
    exportData("/backManage/exportDataCount", config);
  }
  if (resultType.value == "2") {
    exportData("/backManage/exportDataChart", config);
  }
};

const handleReset = () => {
  queryForm.value.pageSize = 10;
  // queryForm.value.projectType = "FBSGF";
  daterangeVal.value = null;
  dataSelectValue.value = "";
  queryForm.value.startTime = null;
  queryForm.value.endTime = null;
  // activeProject.value = project.value[0].value;
  // activeadtaType.value = dataType.value[0].value;
  handleQuery();
};

const handleQuery = () => {
  queryForm.value.pageNum = 1;
  resultType.value = activeadtaType.value;
  if (activeadtaType.value == "2") {
    activeForm.value = { ...queryForm.value, chartId: 201 };
  } else {
    activeForm.value = {
      ...queryForm.value,
      id: dataSelectValue.value ? dataSelectValue.value : null,
    };
  }
  handleGetList();
};

const handleGetList = () => {
  tableLoading.value = true;
  tableData.value = [];
  if (resultType.value == "1") {
    pageDataCount(activeForm.value).then((res) => {
      let data = res.data;
      tableData.value = data.list || [];
      total.value = Number(data.total || 0);
      tableType.value = 1;
      tableLoading.value = false;
    });
  }
  if (resultType.value == "2") {
    pageDataChart(activeForm.value).then((res) => {
      let data = res.data;
      tableData.value = data.list || [];
      tableData.value = tableData.value.map((item) => {
        item.paramValue = Number(item.paramValue);
        return item;
      });
      total.value = Number(data.total || 0);
      tableType.value = 2;
      tableLoading.value = false;
    });
  }
};

const cellClick = (row, column) => {
  if (column.property !== "paramValue") {
    return;
  }
  tableRow.value = row.id;
  nextTick(() => {
    try {
      let _input = document.querySelector(`#input${row.id}`);
      _input.focus();
    } catch (error) {}
  });
};

const handleInputBlur = (row, key, p) => {
  tableRow.value = "";
  const config = {
    id: row.id,
    paramValue: p ? row[key] : row[key].trim(),
  };
  updateDataCount(config).then((res) => {
    ElMessage({
      message: "修改成功",
      type: "success",
    });
    handleGetList();
  });
};

const switchBeforeChange = (params, row) => {
  return new Promise((resolve, reject) => {
    let config = {
      id: row.id,
      ifEnable: row.ifEnable == 0 ? 1 : 0,
    };
    updateDataCount(config)
      .then((res) => {
        ElMessage({
          message: "修改成功",
          type: "success",
        });
        handleGetList();
        return resolve();
      })
      .catch((err) => {
        return reject();
      });
  });
};

const handleSizeChange = (val) => {
  activeForm.value.pageSize = val;
  queryForm.value.pageSize = val;
  handleGetList();
};

const handleCurrentChange = (val) => {
  activeForm.value.pageNum = val;
  queryForm.value.pageNum = val;
  handleGetList();
};
</script>
<template>
  <div class="config">
    <div class="query">
      <div class="query-l">
        <div class="row">
          <div class="label">项目名称:</div>
          <div class="query-info">
            <div
              class="project-item"
              v-for="(item, index) in project || []"
              :key="index"
              @click="handleCheckProject(item)"
              :class="{ projectActive: item.value == activeProject }"
            >
              {{ item.name }}
            </div>
          </div>
        </div>
        <div class="row">
          <div class="label">数据类型:</div>
          <div class="query-info">
            <div
              class="dataType-item"
              v-for="(item, index) in dataType || []"
              :key="index"
              @click="handleCheckData(item)"
              :class="{ dataActive: item.value == activeadtaType }"
            >
              {{ item.name }}
            </div>
          </div>
        </div>
        <div class="row" v-if="activeadtaType == '2'">
          <div class="label">图表名称:</div>
          <div class="query-info">
            <div class="dataType-item dataActive">
              {{ "光电数据实时监测-辐射度" }}
            </div>
          </div>
        </div>
        <div class="row">
          <div class="label">数据筛选:</div>
          <div class="query-info">
            <el-select
              v-model="dataSelectValue"
              filterable
              placeholder="请选择/输入数据名称"
              @change="handleSelectChange"
              clearable
              size="large"
              v-if="activeadtaType != '2'"
            >
              <el-option
                v-for="item in dataSelectList"
                :key="item.id"
                :label="item.paramName"
                :value="item.id"
              />
            </el-select>
            <div class="query-item query-item-date-picker">
              <el-date-picker
                v-model="daterangeVal"
                type="daterange"
                range-separator="-"
                start-placeholder="请选择开始日期"
                end-placeholder="请选择结束日期"
                size="large"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="query-r">
        <div class="row">
          <div class="btn select" @click="handleQuery">查询</div>
          <div class="btn reset" @click="handleReset">重置</div>
          <div class="btn export" @click="handleExport">导出</div>
        </div>
      </div>
    </div>
    <div class="list">
      <el-table
        :data="tableData"
        height="100%"
        style="width: 100%; height: 100%"
        cell-class-name="table-cell"
        @cell-dblclick="cellClick"
        v-loading="tableLoading"
      >
        <el-table-column
          fixed
          prop="paramName"
          label="数据名称"
          align="center"
          row-key="paramName"
          v-if="tableType == 1"
        >
          <template #default="{ row }">
            {{ row.paramName || "" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="paramValue"
          label="数据"
          align="center"
          row-key="paramValue"
          v-if="tableType == 1"
        >
          <template #default="{ row }">
            <span v-if="tableRow != row.id"> {{ row.paramValue }}</span>
            <el-input
              v-model="row.paramValue"
              v-if="tableRow == row.id && !row.ifNumber"
              :id="'input' + row.id"
              :maxlength="10"
              @input="
                row.paramValue = row.paramValue.replace(/[^\u4e00-\u9fa5]/g, '')
              "
              @blur="handleInputBlur(row, 'paramValue')"
            ></el-input>
            <el-input-number
              v-model="row.paramValue"
              v-if="tableRow == row.id && row.ifNumber"
              :id="'input' + row.id"
              :controls="false"
              :precision="2"
              :min="0"
              :max="9999999999.99"
              @blur="(params) => handleInputBlur(row, 'paramValue', params)"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="paramUnit"
          label="单位"
          align="center"
          row-key="paramUnit"
          v-if="tableType == 1"
        >
          <template #default="{ row }">
            {{ row.paramUnit || "" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="ifEnable"
          label="是否已配置数据为准"
          row-key="ifEnable"
          align="center"
          v-if="tableType == 1"
        >
          <template #default="{ row }">
            <el-switch
              v-model="row.ifEnable"
              :active-value="1"
              :inactive-value="0"
              :before-change="(params) => switchBeforeChange(params, row)"
            />
          </template>
        </el-table-column>
        <el-table-column
          fixed
          prop="chartName"
          label="图表名称"
          align="center"
          row-key="chartName"
          v-if="tableType == 2"
        >
          <template #default="{ row }">
            {{ row.chartName || "" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="paramName"
          label="图例名称"
          align="center"
          row-key="paramName"
          v-if="tableType == 2"
        >
          <template #default="{ row }">
            {{ row.paramName || "" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="paramUnit"
          label="单位  "
          align="center"
          row-key="paramUnit"
          v-if="tableType == 2"
        >
          <template #default="{ row }">
            {{ row.paramUnit || "" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="paramKey"
          label="横坐标  "
          align="center"
          row-key="paramKey"
          v-if="tableType == 2"
        >
          <template #default="{ row }">
            {{ row.paramKey || "" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="paramValue"
          label="数值  "
          align="center"
          row-key="paramValue"
          v-if="tableType == 2"
        >
          <template #default="{ row }">
            <span v-if="tableRow != row.id"> {{ row.paramValue }}</span>
            <el-input-number
              v-model="row.paramValue"
              v-if="tableRow == row.id"
              :id="'input' + row.id"
              :controls="false"
              :precision="2"
              :min="0"
              :max="9999999999.99"
              @blur="(params) => handleInputBlur(row, 'paramValue', params)"
            />
            <!-- <el-input
              v-model="row.paramValue"
              v-if="tableRow == row.id"
              :id="'input' + row.id"
              @blur="handleInputBlur(row, 'paramValue')"
            ></el-input> -->
          </template>
        </el-table-column>
        <el-table-column
          prop="updateName"
          label="操作人"
          align="center"
          row-key="updateName"
        >
          <template #default="{ row }">
            {{ row.updateName || "" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="updateTime"
          label="操作时间"
          row-key="updateTime"
          align="center"
        >
          <template #default="{ row }">
            {{ row.updateTime || "" }}
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination
          v-model:current-page="queryForm.pageNum"
          v-model:page-size="queryForm.pageSize"
          :page-sizes="[10, 20, 30, 40]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.config {
  padding: 0 40px;
  display: flex;
  flex-direction: column;
  padding-bottom: 10px;
  .query,
  .list {
    margin-top: 20px;
    background-color: #fff;
    border-radius: 6px;
    padding: 20px 30px;
  }
  .query {
    display: flex;
    align-items: flex-end;
    .query-l {
      width: 1300px;
      .row {
        display: flex;
        align-items: flex-start;
        margin-top: 20px;
        .label {
          width: 94px;
          height: 40px;
          line-height: 40px;
          font-size: 16px;
          color: #333333;
        }
        .query-info {
          width: calc(100% - 94px);
          display: flex;
          flex-wrap: wrap;
          gap: 20px 30px;

          .project-item,
          .dataType-item {
            cursor: pointer;
            background: #ffffff;
            border-radius: 6px;
            border: 1px solid #dcdcdc;
            font-size: 14px;
            color: #666666;
            text-align: center;
            padding: 10px 30px;
          }
          .projectActive,
          .dataActive {
            background: #3873f9;
            color: #ffffff;
            font-weight: bold;
          }
          ::v-deep(.el-select) {
            width: 260px;
          }
          .query-item-date-picker {
            width: 400px;
          }
        }
      }
    }
    .query-r {
      width: calc(100% - 1300px);
      .row {
        display: flex;
        flex-wrap: wrap;
        gap: 20px 30px;
        justify-content: flex-end;
        align-items: center;
        .btn {
          width: 120px;
          height: 40px;
          text-align: center;
          line-height: 40px;
          cursor: pointer;
          border-radius: 4px;
          font-size: 14px;
          user-select: none;
        }
        .select {
          background: #3873f9;
          color: #fff;
          &:active {
            background-color: rgba($color: #3873f9, $alpha: 0.8);
          }
        }
        .reset,
        .export {
          border: 1px solid #1890ff;
          color: #1890ff;
          &:active {
            background-color: rgba($color: #ccc, $alpha: 0.2);
          }
        }
      }
    }
  }
  .list {
    flex: 1;
    overflow: hidden;
    ::v-deep(.el-table) {
      height: calc(100% - 60px) !important;
    }
    ::v-deep(.el-table thead) {
      font-size: 16px;
      color: #323232;
    }
    ::v-deep(.el-table td.el-table__cell div) {
      font-size: 14px;
      color: #666666;
    }
    .pagination {
      display: flex;
      justify-content: flex-end;
      margin-top: 20px;
    }
  }
}
</style>
