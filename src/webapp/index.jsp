<%--
  Created by IntelliJ IDEA.
  User: root
  Date: 2017/1/5
  Time: 下午 02:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="swfobject.js"></script>
    <script type="text/javascript">
        var flashvars = {
        project: "lightning.sb2",
        autostart: "true"
        };
        var flashvars2 = {
            project: "threads.sb2",
            autostart: "true"
        };
        var params = {
        bgcolor: "#FFFFFF",
        allowScriptAccess: "always",
        allowFullScreen: "true",
        wmode: 'window',
        menu: 'false'
        };
        var attributes = {};

        swfobject.embedSWF("Scratch.swf", "flashContent", "100%", "100%", "10.2.0","expressInstall.swf", flashvars,
        params, attributes);
        swfobject.embedSWF("Scratch.swf", "flashContent2", "100%", "100%", "10.2.0","expressInstall.swf", flashvars2,
            params, attributes);
    </script>
</head>

<body>

<div id="flashContent" width="482px" height="387px">
    Get <a href="http://www.adobe.com/go/getflash">Adobe Flash Player</a>, otherwise this Scratch movie will not play.
</div>
<div id="flashContent2" width="482px" height="387px">
    Get <a href="http://www.adobe.com/go/getflash">Adobe Flash Player</a>, otherwise this Scratch movie will not play.
</div>
</body>
</html>
