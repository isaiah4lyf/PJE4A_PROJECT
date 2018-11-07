<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Login.aspx.cs" Inherits="SSS_WEB_APPLICATION.Login" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
</head>
<body>
    <form id="form1" runat="server">
        <div>
			<asp:TextBox ID="username" placeholder="username" runat="server"></asp:TextBox><br/>
			<asp:TextBox ID="password"  runat="server" placeholder="Password" TextMode="Password"></asp:TextBox><br/>
			<asp:Label ID="result" runat="server" Visible="true" ForeColor="Red"></asp:Label><br/>
			<asp:Button ID="sign_in" runat="server" Text="SIGN IN" OnClick="sign_in_Click" />
        </div>
    </form>
</body>
</html>
