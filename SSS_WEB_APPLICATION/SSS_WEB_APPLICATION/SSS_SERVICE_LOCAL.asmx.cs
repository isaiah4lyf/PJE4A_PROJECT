using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using SSS_WEB_APPLICATION.SSS_SERVICE;

namespace SSS_WEB_APPLICATION
{
	/// <summary>
	/// Summary description for SSS_SERVICE_LOCAL
	/// </summary>
	[WebService(Namespace = "http://tempuri.org/")]
	[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
	[System.ComponentModel.ToolboxItem(false)]
	// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
	[System.Web.Script.Services.ScriptService]
	public class SSS_SERVICE_LOCAL : System.Web.Services.WebService
	{

		[WebMethod]
		public List<Device_Coordinate> RETURN_DEVICE_COORDINATE_JS(string Device_Mac) {
			SSS_SERVICE.SSS_SERVICE remote_service = new SSS_SERVICE.SSS_SERVICE();
			return remote_service.RETURN_DEVICE_COORDINATE_JS(Device_Mac).ToList();
		}
	}
}
