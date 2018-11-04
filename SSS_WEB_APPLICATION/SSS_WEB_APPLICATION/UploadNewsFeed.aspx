<%@ Page Title="" Language="C#" MasterPageFile="~/Admin_Master.Master" AutoEventWireup="true" CodeBehind="UploadNewsFeed.aspx.cs" Inherits="SSS_WEB_APPLICATION.UploadNewsFeed" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
	<asp:Label ID="Label1" runat="server" Text="Title"></asp:Label>
	<asp:TextBox ID="title" runat="server"></asp:TextBox><br/>

	<asp:Label ID="Label2" runat="server" Text="Description"></asp:Label>
	<asp:TextBox ID="Description" runat="server"></asp:TextBox><br/>
		
	<asp:Label ID="Label3" runat="server" Text="Read More Link"></asp:Label>
	<asp:TextBox ID="readMoreLink" runat="server"></asp:TextBox><br/>

	<asp:Label ID="Label5" runat="server" Text="Upload Title Image"></asp:Label>
	<asp:FileUpload ID="FileUpload2" runat="server" /><br/>
	
	<asp:Label ID="Label4" runat="server" Text="Upload Video"></asp:Label>
	<asp:FileUpload ID="FileUpload1" runat="server" /><br/>
	<asp:Button ID="Button1" runat="server" Text="Button" OnClick="Button1_Click" />
</asp:Content>
