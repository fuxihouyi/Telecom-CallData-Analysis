<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="js/jquery.min.js"></script>
    <link rel="stylesheet" href="css/layui.css">
    <script src="css/lengthwaysTable.css"></script>
    <script src="js/echarts.min.js"></script>
    <script src="js/purple-passion.js"></script>
    <link rel="stylesheet" href="css/query.css">
    <link rel="stylesheet" href="css/lengthwaysTable.css">
    <title>用户自查中心</title>
    <style>




        body {
            background: radial-gradient(200% 100% at bottom center, #f7f7b6, #e96f92, #75517d, #1b2947);
            background: radial-gradient(220% 105% at top center, #1b2947 10%, #75517d 40%, #e96f92 65%, #f7f7b6);
            background-attachment: fixed;
            overflow: hidden;
        }

        @keyframes rotate {
            0% {
                transform: perspective(400px) rotateZ(20deg) rotateX(-40deg) rotateY(0);
            }
            100% {
                transform: perspective(400px) rotateZ(20deg) rotateX(-40deg) rotateY(-360deg);
            }
        }
        .stars {
            transform: perspective(500px);
            transform-style: preserve-3d;
            position: absolute;
            bottom: 0;
            perspective-origin: 50% 100%;
            left: 50%;
            animation: rotate 90s infinite linear;
        }

        .star {
            width: 2px;
            height: 2px;
            background: #F7F7B6;
            position: absolute;
            top: 0;
            left: 0;
            transform-origin: 0 0 -300px;
            transform: translate3d(0, 0, -300px);
            backface-visibility: hidden;
        }

    </style>
</head>
<body>

<div class="stars"></div>  <!--背景层，不要删除，不然没有作用-->
<div class="All">
    <!-- 用户查询 -->
    <div class="user">
        <h2 style="text-align: center;padding: 5%;">用户查询</h2>
            <div class="phone">
                <form action="">
                    <input id="phone" type="text" name="phone" required lay-verify="required" placeholder="请输入手机号" autocomplete="off" class="layui-input">
                </form>
            </div>
            <div onclick="a()" class="search">
                <span class="choose-font">通话次数</span>
                <img class="choose-img" src="images/consume.png" alt="">
            </div>
            <div onclick="b()" class="search">
                <span class="choose-font">通话时间</span>
                <img class="choose-img" src="images/ct1.png" alt="">
            </div>
            <div onclick="c()" class="search">
                <span class="choose-font">亲密关系</span>
                <img class="choose-img"  src="images/honeypay.png" alt="">
            </div>
            <script>
                function a(){
                    var tel = document.getElementById("phone").value;
                    if(tel.length!=11){
                        alert("手机号码格式错误")
                    }else{
                        window.location.href="/query1?tel="+tel;
                    }
                }
                function b(){
                    var tel = document.getElementById("phone").value;
                    if(tel.length!=11){
                        alert("手机号码格式错误")
                    }else{
                        window.location.href="/query2?tel="+tel;
                    }
                }
                function c(){
                    var tel = document.getElementById("phone").value;
                    if(tel.length!=11){
                        alert("手机号码格式错误")
                    }else{
                        window.location.href="/query3?tel="+tel;
                    }
                }
            </script>

    </div>

    <!-- 用户信息 -->
    <div class="text-date" style="background-color: rgba(128, 128, 128, 0.5);">
        <h2 style="position: absolute;left: 50%;transform: translate(-50%);top: 5%;    color: rgba(205,205,205);
        ">用户信息</h3>
            <table class="mailTable"  cellspacing="0" cellpadding="0">
                <tr>
                    <td class="column">用户总数量</td>
                    <td style="letter-spacing: 1px;">${usercount}</td>
                </tr>
                <tr>
                    <td class="column">通话总时长</td>
                    <td style="letter-spacing: 1px;">${totaldura}</td>
                </tr>

                <tr>
                    <td class="column">通话总次数</td>
                    <td style="letter-spacing: 1px;">${totalcall}</td>
                </tr>
                <tr>
                    <td class="column">最潜力用户</td>
                    <td style="letter-spacing: 1px;">${calllog.name}</td>
                </tr>

                </tr>
            </table>
            <div class="ct-img">
                <img style="height: 100%;width: 100%;" src="images/ct.png" alt="">
            </div>
    </div>



    <div id="t1" class="map-1" style="background-color: rgba(128, 128, 128, 0.5);"></div>
    <div id="t2" class="map-2" style="background-color: rgba(128, 128, 128, 0.5);"></div>

    <!-- 图一 -->
    <script>
        var myChart = echarts.init(document.getElementById('t1'),'purple-passion');
        var option = {
            tooltip: {
                trigger: 'axis',
                position: function (pt) {
                    return [pt[0], '10%'];
                }
            },
            title: {
                left: 'center',
                text: '所有用户每月平均通话总时长',
                textStyle:{
                    color:'#fff'
                }
            },
            toolbox: {
                feature: {
                    dataZoom: {
                        yAxisIndex: 'none'
                    },
                    restore: {},
                    saveAsImage: {}
                }
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: [
                    <c:forEach items="${avglogs}" var="avglog">
                        ${avglog.month} + "月",
                    </c:forEach>
                ]
            },
            yAxis: {
                type: 'value',
                boundaryGap: [0, '100%'],
                axisLabel: {
                    formatter: '{value} 分钟'
                }
            },
            dataZoom: [{
                type: 'inside',
                start: 0,
                end: 10
            }, {
                start: 0,
                end: 10
            }],
            series: [
                {
                    name: '平均通话时长',
                    type: 'line',
                    symbol: 'none',
                    sampling: 'lttb',
                    itemStyle: {
                        color: 'rgb(255, 70, 131)'
                    },
                    areaStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgb(255, 158, 68)'
                        }, {
                            offset: 1,
                            color: 'rgb(255, 70, 131)'
                        }])
                    },
                    data: [
                        <c:forEach items="${avglogs}" var="avglog">
                            ${avglog.avg_dura},
                        </c:forEach>
                    ]
                }
            ]
        };


        myChart.setOption(option);
    </script>

    <!-- 图二 -->
    <script>
        var myChart = echarts.init(document.getElementById('t2'),'purple-passion');
        var option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            title: {
                left: 'center',
                text: '所有用户每月平均通话总次数',
                textStyle: {
                    color:'#fff'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: [
                        <c:forEach items="${avglogs}" var="avglog">
                            ${avglog.month} + "月",
                        </c:forEach>
                    ],
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '平均通话次数',
                    type: 'bar',
                    barWidth: '60%',
                    data: [
                        <c:forEach items="${avglogs}" var="avglog">
                            ${avglog.avg_call},
                        </c:forEach>
                    ]
                }
            ]
        };
        myChart.setOption(option);
    </script>

</div>



<script src="./layui/layui.js"></script>

<script>
    $(document).ready(function(){
        var stars=500;  /*星星的密集程度，数字越大越多*/
        var $stars=$(".stars");
        var r=1000;   /*星星的看起来的距离,值越大越远,可自行调制到自己满意的样子*/
        for(var i=0;i<stars;i++){
            var $star=$("<div/>").addClass("star");
            $stars.append($star);
        }
        $(".star").each(function(){
            var cur=$(this);
            var s=0.2+(Math.random()*1);
            var curR=r+(Math.random()*300);
            cur.css({
                transformOrigin:"0 0 "+curR+"px",
                transform:" translate3d(0,0,-"+curR+"px) rotateY("+(Math.random()*360)+"deg) rotateX("+(Math.random()*-50)+"deg) scale("+s+","+s+")"

            })
        })
    })
</script>

</body>

</html>