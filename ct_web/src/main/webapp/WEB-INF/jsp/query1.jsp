<%@ page contentType="text/html;charset=GBK" language="java" %>
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
    <scrip src="js/westeros.js"></scrip>
    <link rel="stylesheet" href="css/query.css">
    <link rel="stylesheet" href="css/lengthwaysTable.css">
    <link rel="stylesheet" href="css/dropdown.css">
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

    <style>


        .sel_mask{                    /*将<a></a>相对定位*/
            position:absolute;
            width:60%;
            height:70%;
            background:rgba(129, 129, 129, 0.6);;
            border-radius: 5px;
            box-shadow:0px 0px 5px rgba(128, 128, 128, 0.6);
            display:inline-block;
            text-decoration: none;
            cursor: pointer;
            right: 5%;
            top: 50%;
            transform: translate(0,-50%);
        }
        .sel_mask:hover{         /*添加hover效果*/
            background:rgb(139, 139, 139);
            cursor: pointer;
        }




    </style>
</head>
<body>
<div class="stars"></div>  <!--背景层，不要删除，不然没有作用-->
<div class="All">
    <div class="user">
        <h2 style="text-align: center;padding: 5%;letter-spacing: 6px;color: white;text-shadow: 0 0 7px black;">用户查询</h2>
            <div class="phone">
                <form action="">
                    <input id="phone" type="text" name="phone" required lay-verify="required" placeholder="请输入手机号" autocomplete="off" class="layui-input">
                </form>
            </div>

            <script>
                var setPhone = document.location.href.split("=")[1].split("&")[0];
                if (setPhone!=undefined){
                    document.getElementById('phone').value = setPhone;
                }
            </script>

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
    <div class="selector">
        <span style="position: absolute;left: 10%;top: 25%;color: rgb(205, 205, 205);">请选择月份</span>
        <div class="selector-inner">
            <div class="dropdown">
                <button class="dropbtn" onclick="smonth1()">月份</button>
                <div id="smonth" class="dropdown-content">
<%--                    <a id="a" onclick="one(this)">1 月</a>--%>
<%--                    <a id="b" onclick="one(this)">2 月</a>--%>
<%--                    <a id="c" onclick="one(this)">3 月</a>--%>
<%--                    <a id="d" onclick="one(this)">4 月</a>--%>
<%--                    <a id="e" onclick="one(this)">5 月</a>--%>
<%--                    <a id="f" onclick="one(this)">6 月</a>--%>
<%--                    <a id="g" onclick="one(this)">7 月</a>--%>
<%--                    <a id="h" onclick="one(this)">8 月</a>--%>
<%--                    <a id="i" onclick="one(this)">9 月</a>--%>
<%--                    <a id="j" onclick="one(this)">10 月</a>--%>
<%--                    <a id="k" onclick="one(this)">11 月</a>--%>
<%--                    <a id="l" onclick="one(this)">12 月</a>--%>
                </div>
            </div>
        </div>
    </div>
    <script>
        function smonth1(){
            var htmlle = '';

            var i;
            for(var i in ${calllogs}) {
                htmlle += "<a id = '" + ${calllogs}[i].month +"' onclick='one(this)'>"+ ${calllogs}[i].month +" 月</a>";
            }

            document.getElementById("smonth").innerHTML=htmlle;
        }
    </script>
    <script>
        function one(obj){
            var tid = $(obj).attr("id");
            var month = document.getElementById(tid).innerText.split(" ")[0];

            var strRight = document.location.href.split("?")[1].split("=")[1].split("&")[0];

            var thref = document.location.href.split("?")[0];

            window.location.href=thref+"_1"+"?tel="+strRight+"&month="+month;
        }
    </script>
    <div class="text-date" style="background-color: rgba(128, 128, 128, 0.5);">
        <h2 style="letter-spacing: 6px;position: absolute;left: 50%;transform: translate(-50%);top: 5%;    color: rgba(205,205,205);
        color: white;text-shadow: 0 0 7px black;">用户信息</h2>
            <table class="mailTable"  cellspacing="0" cellpadding="0">
                <tr>
                    <td class="column">&nbsp用户&nbsp姓名 &nbsp </td>
                    <td class="column2">${name}</td>
                </tr>
                <tr>
                    <td class="column">&nbsp用户&nbsp号码 &nbsp </td>
                    <td class="column2">${tel}</td>
                </tr>

                <tr>
                    <td class="column">&nbsp号码&nbsp所属 &nbsp </td>
                    <td class="column2">${sessionScope.geo}</td>
                </tr>
                <tr>
                    <td class="column">通话总次数</td>
                    <td class="column2">${sumcall}</td>
                </tr>
                <tr>
                    <td class="column">通话总时长</td>
                    <td class="column2">${sumdura}</td>
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
        var data = ${calllogs};
        var sumcall = [];
        for (key in data){
            sumcall.push({name:data[key].month+"月",value:data[key].sumcall});
        }
        console.log(sumcall);
        var option = {
            title: {
                text: '通话次数',
                left: 'center',
                textStyle:{
                    color: '#fff'
                }

            },
            tooltip: {
                trigger: 'item'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
            },
            series: [
                {
                    name: '通话次数',
                    type: 'pie',
                    radius: '50%',
                    data: sumcall,
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };


        myChart.setOption(option);
    </script>

    <!-- 图二 -->
    <script>
        var myChart = echarts.init(document.getElementById('t2'),'westeros');
        var data = ${calllogs};
        var sumcall2 = [];
        sumcall2.push(['score', 'amount', 'product']);
        for (key in data){
            sumcall2.push([Math.random()*100,data[key].sumcall,data[key].month+"月"])
        }
        console.log(sumcall2);
        var option = {
            dataset: {
                source: sumcall2
            },
            grid: {containLabel: true},
            xAxis: {name: 'amount',
                axisLabel: {
                    color: '#9388e5',
                    textStyle:{
                        color:'#fff',
                        fontSize:16,//坐标的字体颜色
                    },
                }},
            yAxis: {type: 'category',
                axisLabel: {
                    color: '#9388e5',
                    textStyle:{
                        color:'#fff',
                        fontSize:16,//坐标的字体颜色
                    },
                }},
            visualMap: {
                orient: 'horizontal',
                left: 'center',
                min: 10,
                max: 100,
                text: ['High Score', 'Low Score'],
                textStyle:{
                  color:"#fff",
                },
                // Map the score column to color
                dimension: 0,
                inRange: {
                    color: ['#a5e7f0', '#7ac9c4', 'rgb(90,200,235)']
                }
            },
            series: [
                {
                    type: 'bar',
                    encode: {
                        // Map the "amount" column to X axis.
                        x: 'amount',
                        textStyle:{
                              color:'#fff'
                        },
                        // Map the "product" column to Y axis
                        y: 'product'
                    }
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
