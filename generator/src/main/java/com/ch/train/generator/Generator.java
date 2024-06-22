package com.ch.train.generator;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

/**
 * author: ch
 * create: 2024--2211:54
 * Description:
 */
public class Generator {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/train?characterEncoding=UTF8&autoReconnect=true&serverTimezone=Asia/Shanghai";
        String username = "train";
        String password = "train";
        FastAutoGenerator.create(url ,username, password)
                .globalConfig(builder -> {
                    builder.author("ch") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .commentDate("yyyy-MM-dd")
                            .outputDir(System.getProperty("user.dir")+"\\src\\main\\java"); // 指定输出目录

                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
//                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
//                            if (typeCode == Types.SMALLINT) {
//                                // 自定义类型转换
//                                return DbColumnType.INTEGER;
//                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("com.ch.train") // 设置父包名
                                .moduleName("train") // 设置父包模块名
                                .entity("domain") // 设置 Entity 包名
                                .service("service") // 设置 Service 包名
                                .serviceImpl("service.impl") // 设置 Service Impl 包名
                                .mapper("mapper") // 设置 Mapper 包名
                                .xml("mappers") // 设置 Mapper XML 包名
                                .controller("controller") // 设置 Controller 包名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir")+"\\src\\main\\resources\\mappers")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder ->
                        builder.addInclude("member" )
                                .addTablePrefix("p_")
                                .serviceBuilder()
                                .formatServiceFileName("%sService")
                                .formatServiceImplFileName("%sServiceImpl")
                                .entityBuilder()
                                .enableLombok()
                                .logicDeleteColumnName("deleted")
                                .enableTableFieldAnnotation()
                                .controllerBuilder()
                                // 映射路径使用连字符格式，而不是驼峰
                                .enableHyphenStyle()
                                .formatFileName("%sController")
                                .enableRestStyle()
                                .mapperBuilder()
                                //生成通用的resultMap
                                .enableBaseResultMap()
                                .superClass(BaseMapper.class)
                                .formatMapperFileName("%sMapper")
                                .enableMapperAnnotation()
                                .formatXmlFileName("%sMapper")
                )

                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
