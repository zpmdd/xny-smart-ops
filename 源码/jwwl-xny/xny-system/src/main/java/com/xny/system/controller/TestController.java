package com.xny.system.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xny.common.utils.R;
import com.xny.common.utils.SnowIdUtil;
import com.xny.system.entity.DataParamHistory;
import com.xny.system.service.IDataParamHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

@RestController
@RequestMapping("/dataParamHistory")
@Api(tags = "分表测试")
public class TestController {
    @Resource
    private IDataParamHistoryService dataParamHistoryService;

    @ApiOperation(value = "添加数据")
    @PostMapping("/addData")
    public R<String> addData(@RequestParam(value = "dateStr") String dateStr){
        DataParamHistory dataParamHistory = new DataParamHistory();
        dataParamHistory.setId(SnowIdUtil.nextId());
        dataParamHistory.setParamId(1L);
        dataParamHistory.setParamValue(20d);
        dataParamHistory.setParamTime(DateUtil.parse(dateStr,"yyyy-MM-dd"));
        dataParamHistoryService.save(dataParamHistory);
        return R.ok();
    }

    @ApiOperation(value = "数据列表")
    @PostMapping("/listData")
    public R<PageInfo<DataParamHistory>> listData(){
        PageHelper.startPage(1,10);
        List<DataParamHistory> list = dataParamHistoryService.list(
                new LambdaQueryWrapper<DataParamHistory>()
                        .ge(DataParamHistory::getParamTime,DateUtil.parse("2024-01-01","yyyy-MM-dd"))
                        .le(DataParamHistory::getParamTime,DateUtil.endOfDay(new Date()))
        );
        return R.ok(new PageInfo<>(list));
    }

    public static void main(String[] args) throws Exception{
        Path directoryPath = Paths.get("C:\\Users\\admin\\Desktop\\主站入库点表");
        DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directoryPath);
        List<List<String>> rows = new ArrayList<>();

        for (Path path : directoryStream) {
            Reader reader = new FileReader(path.toString());
            BufferedReader br = new BufferedReader(reader);
            String line;
            String type = "";
            while ((line = br.readLine()) != null) {
                if(line.contains("[遥")){
                    type = line;
                }

                List<String> row = new ArrayList<>();
                String regex = ",+";
                String s = line.replaceAll(regex, ",");
                String[] split = s.split(",");
                if(split.length == 2){
                    row.add(type.replace("[","").replace("]",""));

                    for (int i = 0; i < split.length; i++) {
                        row.add(split[i]);
//                        if(i != 2){
//                            row.add(split[i]);
//                        }else {
//                            String[] split1 = split[2].split("/");
//                            List<String> collect = Arrays.stream(split1).collect(Collectors.toList());
//                            if(collect.size()==3){
//                                collect.add("");
//                            }
//                            row.addAll(collect);
//                        }
                    }

                    rows.add(row);
                }
            }
        }

        //通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter("C:\\Users\\admin\\Desktop\\站点.xlsx");
        //一次性写出内容，强制输出标题
        writer.write(rows, true);
        //关闭writer，释放内存
        writer.close();

    }
}
