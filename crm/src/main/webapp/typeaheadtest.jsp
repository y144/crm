<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<html>
<head>
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
    <base href="<%=basePath%>">
    <title>演示bs_typeahead插件</title><%--自动补全插件--%>
    <script type="text/javascript">
        $(function () {
            //当容器加载完成，对容器调用工具函数
            var name2id={};//定义一个数组用来存放，将查得到的客户名与对应的id以key=value的形式进行保存，将id传给后台，可以根据id进行查找
            $("#customerName").typeahead({
                source:function (query, process) {
                    //在每次键盘弹起时都会执行本函数，可以在函数中生成一个数据源(字符串数组)，交给source参数使用，source每次拿到字符串数组时，都会根据输入的关键字进行对比，比对成功的字符串，都会在容器下方展示，从而实现自动补全
                    //query：是用户在容器中输入的关键字
                    //process:是一个bs_typeahead插件提供的函数，能够将拿到的比对成功的字符串数组，通过process函数，把一个json格式的字符串数组交给source使用

                    //向后台发送异步请求，查询客户名称组成的字符串数组，以json格式的字符串的形式返回，交给source使用
                    $.ajax({
                        url:'workbench/transaction/typeahead.do',
                        data:{
                            customerName:query
                        },
                        type:'post',
                        dataType:'json',
                        success:function (data) {//data就是json格式的字符串数组
                            var customerNameArr=[];
                            //遍历data复杂类型的数组
                            $.each(data,function (index, obj) {
                                //生成简单类型数据
                                customerNameArr.push(obj.name)
                                //把obj的name和id赋值给name2id，把name作为name2id的属性，id作为name2id的属性值
                                name2id[obj.name]=obj.id;
                            });
                            //将得到的name数组交给函数process,由该函数将数据交给source使用
                            process(customerNameArr);
                        }
                    });
                },
                afterSelect:function (item) {
                    alert(name2id[item]);
                }
            });
        });
    </script>
</head>
<body>
<input type="text" id="customerName">
</body>
</html>
