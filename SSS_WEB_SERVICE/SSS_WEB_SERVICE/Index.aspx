<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Index.aspx.cs" Inherits="SSS_WEB_SERVICE.Index" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <script type="text/javascript" language="javascript">
        function GetData() {
            SSS_WEB_SERVICE.SSS_SERVICE.RETURN_DEVICE_COORDINATE_JS("7C:2E:DD:F5:6C:C4",getResults);
        }

        function getResults(results) {
            
            alert(results[0].Longitude + " " + results[0].Latitude);
        }
    </script>
</head>
<body>
    <form id="form1" runat="server">
    <asp:ScriptManager ID="ScriptManager1" runat="server">
			<Services>
			<asp:ServiceReference Path="http://smartphonesecuritysystem.dedicated.co.za:8080/SSS_SERVICE.asmx"/>
			</Services>
		</asp:ScriptManager>
        <div>
            <input id="button1" type="button" value="getresults" onclick="GetData()"/> 
        </div>
    </form>
</body>
</html>
