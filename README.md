
### 数据拟合 

```` java
        // 收集数据。
        最终WeightedObservedPoints obs = new WeightedObservedPoints（）;
        obs.add（-1.00,2.021170021833143）;
        obs.add（-0.99,2.221135431136975）;
        obs.add（-0.98,2.09985277659314）;
        obs.add（-0.97,2.0211192647627025）;
        // ...省略了很多行...
        obs.addt（0.99，-2.4345814727089854）;
        
        //实例化三次多项式拟合器。
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create（3）;
        
        //检索拟合参数（多项式函数的系数）。
        final double [] coeff = fitter.fit（obs.toList（））;
        
````
- [参考地址](http://commons.apache.org/proper/commons-math/userguide/fitting.html):http://commons.apache.org/proper/commons-math/userguide/fitting.html

- 前端拟合 
```
 Polynomial Regression
     <script src='https://cdn.bootcss.com/echarts/3.4.0/echarts.js'></script>
     <script src='./dist/ecStat.js'></script>
     <script>
     
     var myRegression = ecStat.regression('polynomial', data, 3);
 
 </script>
 polynomial regression
 
regression
```
- [多项式拟合](https://github.com/ecomfe/echarts-stat/blob/master/README.zh-CN.md)


- 大数据量导入
- 560724 只需要 18秒 