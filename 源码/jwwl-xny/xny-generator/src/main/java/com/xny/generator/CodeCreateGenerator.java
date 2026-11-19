package com.xny.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;


public class CodeCreateGenerator {
    //数据库链接信息
    private static final String url = System.getenv().getOrDefault("XNY_GENERATOR_JDBC_URL", "jdbc:mysql://127.0.0.1:3306/xny_system_local");
    private static final String username = System.getenv().getOrDefault("XNY_DB_USER", "xny_local");
    private static final String password = System.getenv().getOrDefault("XNY_DB_PASSWORD", "");

    //文件输出路径
    private static final String filePath = System.getenv().getOrDefault("XNY_GENERATOR_OUTPUT_DIR", "./target/generated-sources");
    //项目包名
    private static final String packageName = "com.xny.system";
    //要生成代码的数据库表名 (多个表用逗号隔开)
    private static final String tableNames = "data_qd_warn";
    //去除数据库表前缀（无需去除前缀时为空即可）
    private static final String[] tablePrefix= {};


    public static void main(String[] args) {
        FileUtil.del(filePath);

        FastAutoGenerator.create(
            new DataSourceConfig.Builder(
                    url, username, password)
            )
            .globalConfig(builder -> {
                builder.author("jwwl") // 设置作者
                        //.disableOpenDir() //禁止打开输出目录
                        .enableSwagger() // 开启 swagger 模式
                        .dateType(DateType.ONLY_DATE)
                        .outputDir(filePath); // 指定输出目录
            })
            .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                int typeCode = metaInfo.getJdbcType().TYPE_CODE;

                //自定义类型转换
                if (typeCode == Types.SMALLINT || typeCode == Types.BIT || typeCode == Types.TINYINT) {
                    return DbColumnType.INTEGER;
                }else if(typeCode == Types.DOUBLE){
                    return DbColumnType.DOUBLE;
                }

                return typeRegistry.getColumnType(metaInfo);

            }))
            .packageConfig(builder -> {
                builder.parent(packageName) //设置父包名
                        .pathInfo(Collections.singletonMap(OutputFile.xml,
                                filePath+"/resources/mapper")); // 设置mapperXml生成路径
            })
            .strategyConfig(builder -> {
                builder.addInclude(tableNames); //设置需要生成的表名

                if(ArrayUtil.isNotEmpty(tablePrefix)){
                    builder.addTablePrefix(tablePrefix); //设置过滤表前缀
                }

                builder.entityBuilder()
                        .enableChainModel()
                        .enableLombok()
                        .idType(IdType.ASSIGN_ID);
            })
            .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
            .execute();

    }


}
