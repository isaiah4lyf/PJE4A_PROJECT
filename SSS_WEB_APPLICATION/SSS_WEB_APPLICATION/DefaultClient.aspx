<%@ Page Title="" Language="C#" MasterPageFile="~/Client_Master.Master" AutoEventWireup="true" CodeBehind="DefaultClient.aspx.cs" Inherits="SSS_WEB_APPLICATION.DefaultClient" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
	<asp:HyperLink ID="Device_Tracker" runat="server">Device Tracker</asp:HyperLink>
	<asp:HyperLink ID="Training_Images" runat="server">Training Images</asp:HyperLink>
	<asp:HyperLink ID="Prediction_Images" runat="server">Prediction Images</asp:HyperLink>
	<asp:HyperLink ID="Upload_News_Feed" runat="server">Upload News Feeds</asp:HyperLink>
</asp:Content>
