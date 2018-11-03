<%@ Page Title="" Language="C#" MasterPageFile="~/Client_Master.Master" AutoEventWireup="true" CodeBehind="TrackerClient.aspx.cs" Inherits="SSS_WEB_APPLICATION.TrackerClient" %>
<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
	<style>
      #map {
        height: 600px;
		width:800px;
      }

    </style>
	<script src="ServerJavasripts/TrackerClientScripts/StopRingingDevice.js"></script>
	<script src="ServerJavasripts/TrackerClientScripts/RingDevice.js"></script>
	<script src="ServerJavasripts/TrackerClientScripts/InitializeMap.js"></script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
	<div>
		<input id="button1" type="button" value="RING DEVICE" onclick="RING_DEVICE()"/> 
		<input id="button2" type="button" value="STOP RINGING DEVICE" onclick="STOP_RINGING_DEVICE()"/> 
	</div>
	<div id="map"></div>
</asp:Content>
<asp:Content ID="Content3" ContentPlaceHolderID="Scripts" runat="server">
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDI63wmkmE0fWNWgv4tCOB3cALl-W5N2RI&callback=initMap"
    async defer></script>
</asp:Content>

