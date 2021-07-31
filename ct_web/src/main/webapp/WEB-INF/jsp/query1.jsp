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
    <title>�û��Բ�����</title>

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


        .sel_mask{                    /*��<a></a>��Զ�λ*/
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
        .sel_mask:hover{         /*���hoverЧ��*/
            background:rgb(139, 139, 139);
            cursor: pointer;
        }




    </style>
</head>
<body>
<div class="stars"></div>  <!--�����㣬��Ҫɾ������Ȼû������-->
<div class="All">
    <div class="user">
        <h2 style="text-align: center;padding: 5%;letter-spacing: 6px;color: white;text-shadow: 0 0 7px black;">�û���ѯ</h2>
            <div class="phone">
                <form action="">
                    <input id="phone" type="text" name="phone" required lay-verify="required" placeholder="�������ֻ���" autocomplete="off" class="layui-input">
                </form>
            </div>

            <script>
                var setPhone = document.location.href.split("=")[1].split("&")[0];
                if (setPhone!=undefined){
                    document.getElementById('phone').value = setPhone;
                }
            </script>

            <div onclick="a()" class="search">
                <span class="choose-font">ͨ������</span>
                <img class="choose-img" src="images/consume.png" alt="">
            </div>
            <div onclick="b()" class="search">
                <span class="choose-font">ͨ��ʱ��</span>
                <img class="choose-img" src="images/ct1.png" alt="">
            </div>
            <div onclick="c()" class="search">
                <span class="choose-font">���ܹ�ϵ</span>
                <img class="choose-img"  src="images/honeypay.png" alt="">
            </div>
            <script>
                function a(){
                    var tel = document.getElementById("phone").value;
                    if(tel.length!=11){
                        alert("�ֻ������ʽ����")
                    }else{
                        window.location.href="/query1?tel="+tel;
                    }
                }
                function b(){
                    var tel = document.getElementById("phone").value;
                    if(tel.length!=11){
                        alert("�ֻ������ʽ����")
                    }else{
                        window.location.href="/query2?tel="+tel;
                    }
                }
                function c(){
                    var tel = document.getElementById("phone").value;
                    if(tel.length!=11){
                        alert("�ֻ������ʽ����")
                    }else{
                        window.location.href="/query3?tel="+tel;
                    }
                }
            </script>

    </div>
    <div class="selector">
        <span style="position: absolute;left: 10%;top: 25%;color: rgb(205, 205, 205);">��ѡ���·�</span>
        <div class="selector-inner">
            <div class="dropdown">
                <button class="dropbtn" onclick="smonth1()">�·�</button>
                <div id="smonth" class="dropdown-content">
<%--                    <a id="a" onclick="one(this)">1 ��</a>--%>
<%--                    <a id="b" onclick="one(this)">2 ��</a>--%>
<%--                    <a id="c" onclick="one(this)">3 ��</a>--%>
<%--                    <a id="d" onclick="one(this)">4 ��</a>--%>
<%--                    <a id="e" onclick="one(this)">5 ��</a>--%>
<%--                    <a id="f" onclick="one(this)">6 ��</a>--%>
<%--                    <a id="g" onclick="one(this)">7 ��</a>--%>
<%--                    <a id="h" onclick="one(this)">8 ��</a>--%>
<%--                    <a id="i" onclick="one(this)">9 ��</a>--%>
<%--                    <a id="j" onclick="one(this)">10 ��</a>--%>
<%--                    <a id="k" onclick="one(this)">11 ��</a>--%>
<%--                    <a id="l" onclick="one(this)">12 ��</a>--%>
                </div>
            </div>
        </div>
    </div>
    <script>
        function smonth1(){
            var htmlle = '';

            var i;
            for(var i in ${calllogs}) {
                htmlle += "<a id = '" + ${calllogs}[i].month +"' onclick='one(this)'>"+ ${calllogs}[i].month +" ��</a>";
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
        color: white;text-shadow: 0 0 7px black;">�û���Ϣ</h2>
            <table class="mailTable"  cellspacing="0" cellpadding="0">
                <tr>
                    <td class="column">&nbsp�û�&nbsp���� &nbsp </td>
                    <td class="column2">${name}</td>
                </tr>
                <tr>
                    <td class="column">&nbsp�û�&nbsp���� &nbsp </td>
                    <td class="column2">${tel}</td>
                </tr>

                <tr>
                    <td class="column">&nbsp����&nbsp���� &nbsp </td>
                    <td class="column2">${sessionScope.geo}</td>
                </tr>
                <tr>
                    <td class="column">ͨ���ܴ���</td>
                    <td class="column2">${sumcall}</td>
                </tr>
                <tr>
                    <td class="column">ͨ����ʱ��</td>
                    <td class="column2">${sumdura}</td>
                </tr>
            </table>
            <div class="ct-img">
                <img style="height: 100%;width: 100%;" src="images/ct.png" alt="">
            </div>
    </div>

    <div id="t1" class="map-1" style="background-color: rgba(128, 128, 128, 0.5);"></div>
    <div id="t2" class="map-2" style="background-color: rgba(128, 128, 128, 0.5);"></div>

    <!-- ͼһ -->
    <script>
        var myChart = echarts.init(document.getElementById('t1'),'purple-passion');
        var data = ${calllogs};
        var sumcall = [];
        for (key in data){
            sumcall.push({name:data[key].month+"��",value:data[key].sumcall});
        }
        console.log(sumcall);
        var option = {
            title: {
                text: 'ͨ������',
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
                    name: 'ͨ������',
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

    <!-- ͼ�� -->
    <script>
        var myChart = echarts.init(document.getElementById('t2'),'westeros');
        var data = ${calllogs};
        var sumcall2 = [];
        sumcall2.push(['score', 'amount', 'product']);
        for (key in data){
            sumcall2.push([Math.random()*100,data[key].sumcall,data[key].month+"��"])
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
                        fontSize:16,//�����������ɫ
                    },
                }},
            yAxis: {type: 'category',
                axisLabel: {
                    color: '#9388e5',
                    textStyle:{
                        color:'#fff',
                        fontSize:16,//�����������ɫ
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
        var stars=500;  /*���ǵ��ܼ��̶ȣ�����Խ��Խ��*/
        var $stars=$(".stars");
        var r=1000;   /*���ǵĿ������ľ���,ֵԽ��ԽԶ,�����е��Ƶ��Լ����������*/
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
