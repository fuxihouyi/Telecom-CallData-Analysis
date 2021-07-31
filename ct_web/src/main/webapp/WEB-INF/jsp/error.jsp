<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="js/jquery.min.js"></script>
    <link rel="stylesheet" href="./css/light_bulb.css">

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

        .title{
            position: absolute;
            left: 50%;
            top: 15%;
            transform: translate(-50%,-50%);
        }

        .title span{
            font-size:40px;
            color: #d4d4d4;
            font: optional;
            letter-spacing: 10px;
            text-shadow: 0px 0px 15px #7cc4ff ;
        }
    </style>

</head>
<body>
<div class="stars"></div>  <!--背景层，不要删除，不然没有作用-->


<div onclick="window.open('/query')" class="light_bulb" style="position: absolute;left: 50%;top: 50%;transform: translate(-50%,-50%);">
    <span style="--i:1;font-size: 40px;">用</span>
    <span style="--i:2;font-size: 40px;">户</span>
    <span style="--i:3;font-size: 40px;">不</span>
    <span style="--i:4;font-size: 40px;">存</span>
    <span style="--i:5;font-size: 40px;">在</span>
    <span style="--i:6;font-size: 40px;">,</span>
    <span style="--i:7;font-size: 40px;">3</span>
    <span style="--i:8;font-size: 40px;">秒</span>
    <span style="--i:9;font-size: 40px;">后</span>
    <span style="--i:10;font-size: 40px;">返</span>
    <span style="--i:11;font-size: 40px;">回</span>
</div>



<script type="text/javascript">
    setTimeout("hidediv()", 3000);
    function hidediv(){
        window.location.href="/query"
    }
</script>

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
