## POI实现 50万条数据导入 
+ 导入 56万条只需要18s 左右

+ 实现原理
    + 多线程
    + Mybatis批量

+ 测试硬件环境
    + win10
    + intel i5 双核 8G内存
    
+ 软件环境
    + Springboot2.0 +
    + MyBatis
    + Mysql 5.7 + 
    
+ 运行
    +  http://localhost:7001/ 
+ 数据库表
    +  建库 
    
    ```` SQL
    
    SET FOREIGN_KEY_CHECKS=0;
    
    -- ----------------------------
    -- Table structure for import_data
    -- ----------------------------
    DROP TABLE IF EXISTS `import_data`;
    CREATE TABLE `import_data` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `name` varchar(32) DEFAULT NULL,
      `no` int(11) DEFAULT NULL,
      `grade` varchar(20) DEFAULT NULL,
      `age` int(11) DEFAULT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=560725 DEFAULT CHARSET=utf8mb4;

    

    ````