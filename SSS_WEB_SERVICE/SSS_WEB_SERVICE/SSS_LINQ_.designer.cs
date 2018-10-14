﻿#pragma warning disable 1591
//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated by a tool.
//     Runtime Version:4.0.30319.42000
//
//     Changes to this file may cause incorrect behavior and will be lost if
//     the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace SSS_WEB_SERVICE
{
	using System.Data.Linq;
	using System.Data.Linq.Mapping;
	using System.Data;
	using System.Collections.Generic;
	using System.Reflection;
	using System.Linq;
	using System.Linq.Expressions;
	using System.ComponentModel;
	using System;
	
	
	[global::System.Data.Linq.Mapping.DatabaseAttribute(Name="SSS_DATABASE_")]
	public partial class SSS_LINQ_DataContext : System.Data.Linq.DataContext
	{
		
		private static System.Data.Linq.Mapping.MappingSource mappingSource = new AttributeMappingSource();
		
    #region Extensibility Method Definitions
    partial void OnCreated();
    partial void InsertImage(Image instance);
    partial void UpdateImage(Image instance);
    partial void DeleteImage(Image instance);
    partial void InsertTrained_Model(Trained_Model instance);
    partial void UpdateTrained_Model(Trained_Model instance);
    partial void DeleteTrained_Model(Trained_Model instance);
    partial void InsertTrained_Models_Interaction_Data(Trained_Models_Interaction_Data instance);
    partial void UpdateTrained_Models_Interaction_Data(Trained_Models_Interaction_Data instance);
    partial void DeleteTrained_Models_Interaction_Data(Trained_Models_Interaction_Data instance);
    partial void InsertTrained_Models_Voice_Note(Trained_Models_Voice_Note instance);
    partial void UpdateTrained_Models_Voice_Note(Trained_Models_Voice_Note instance);
    partial void DeleteTrained_Models_Voice_Note(Trained_Models_Voice_Note instance);
    partial void InsertVoice_Note(Voice_Note instance);
    partial void UpdateVoice_Note(Voice_Note instance);
    partial void DeleteVoice_Note(Voice_Note instance);
    partial void InsertUser(User instance);
    partial void UpdateUser(User instance);
    partial void DeleteUser(User instance);
    partial void InsertAccuracy_User(Accuracy_User instance);
    partial void UpdateAccuracy_User(Accuracy_User instance);
    partial void DeleteAccuracy_User(Accuracy_User instance);
    partial void InsertDevices_Mac(Devices_Mac instance);
    partial void UpdateDevices_Mac(Devices_Mac instance);
    partial void DeleteDevices_Mac(Devices_Mac instance);
    partial void InsertCurrent_Num_Image(Current_Num_Image instance);
    partial void UpdateCurrent_Num_Image(Current_Num_Image instance);
    partial void DeleteCurrent_Num_Image(Current_Num_Image instance);
    #endregion
		
		public SSS_LINQ_DataContext() : 
				base(global::System.Configuration.ConfigurationManager.ConnectionStrings["SSS_DATABASE_ConnectionString"].ConnectionString, mappingSource)
		{
			OnCreated();
		}
		
		public SSS_LINQ_DataContext(string connection) : 
				base(connection, mappingSource)
		{
			OnCreated();
		}
		
		public SSS_LINQ_DataContext(System.Data.IDbConnection connection) : 
				base(connection, mappingSource)
		{
			OnCreated();
		}
		
		public SSS_LINQ_DataContext(string connection, System.Data.Linq.Mapping.MappingSource mappingSource) : 
				base(connection, mappingSource)
		{
			OnCreated();
		}
		
		public SSS_LINQ_DataContext(System.Data.IDbConnection connection, System.Data.Linq.Mapping.MappingSource mappingSource) : 
				base(connection, mappingSource)
		{
			OnCreated();
		}
		
		public System.Data.Linq.Table<Image> Images
		{
			get
			{
				return this.GetTable<Image>();
			}
		}
		
		public System.Data.Linq.Table<Trained_Model> Trained_Models
		{
			get
			{
				return this.GetTable<Trained_Model>();
			}
		}
		
		public System.Data.Linq.Table<Trained_Models_Interaction_Data> Trained_Models_Interaction_Datas
		{
			get
			{
				return this.GetTable<Trained_Models_Interaction_Data>();
			}
		}
		
		public System.Data.Linq.Table<Trained_Models_Voice_Note> Trained_Models_Voice_Notes
		{
			get
			{
				return this.GetTable<Trained_Models_Voice_Note>();
			}
		}
		
		public System.Data.Linq.Table<Voice_Note> Voice_Notes
		{
			get
			{
				return this.GetTable<Voice_Note>();
			}
		}
		
		public System.Data.Linq.Table<User> Users
		{
			get
			{
				return this.GetTable<User>();
			}
		}
		
		public System.Data.Linq.Table<Accuracy_User> Accuracy_Users
		{
			get
			{
				return this.GetTable<Accuracy_User>();
			}
		}
		
		public System.Data.Linq.Table<Devices_Mac> Devices_Macs
		{
			get
			{
				return this.GetTable<Devices_Mac>();
			}
		}
		
		public System.Data.Linq.Table<Current_Num_Image> Current_Num_Images
		{
			get
			{
				return this.GetTable<Current_Num_Image>();
			}
		}
	}
	
	[global::System.Data.Linq.Mapping.TableAttribute(Name="dbo.Images")]
	public partial class Image : INotifyPropertyChanging, INotifyPropertyChanged
	{
		
		private static PropertyChangingEventArgs emptyChangingEventArgs = new PropertyChangingEventArgs(String.Empty);
		
		private int _Id;
		
		private string _Image_Path;
		
		private System.Nullable<int> _User_ID;
		
    #region Extensibility Method Definitions
    partial void OnLoaded();
    partial void OnValidate(System.Data.Linq.ChangeAction action);
    partial void OnCreated();
    partial void OnIdChanging(int value);
    partial void OnIdChanged();
    partial void OnImage_PathChanging(string value);
    partial void OnImage_PathChanged();
    partial void OnUser_IDChanging(System.Nullable<int> value);
    partial void OnUser_IDChanged();
    #endregion
		
		public Image()
		{
			OnCreated();
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Id", AutoSync=AutoSync.OnInsert, DbType="Int NOT NULL IDENTITY", IsPrimaryKey=true, IsDbGenerated=true)]
		public int Id
		{
			get
			{
				return this._Id;
			}
			set
			{
				if ((this._Id != value))
				{
					this.OnIdChanging(value);
					this.SendPropertyChanging();
					this._Id = value;
					this.SendPropertyChanged("Id");
					this.OnIdChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Image_Path", DbType="NVarChar(MAX)")]
		public string Image_Path
		{
			get
			{
				return this._Image_Path;
			}
			set
			{
				if ((this._Image_Path != value))
				{
					this.OnImage_PathChanging(value);
					this.SendPropertyChanging();
					this._Image_Path = value;
					this.SendPropertyChanged("Image_Path");
					this.OnImage_PathChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_User_ID", DbType="Int")]
		public System.Nullable<int> User_ID
		{
			get
			{
				return this._User_ID;
			}
			set
			{
				if ((this._User_ID != value))
				{
					this.OnUser_IDChanging(value);
					this.SendPropertyChanging();
					this._User_ID = value;
					this.SendPropertyChanged("User_ID");
					this.OnUser_IDChanged();
				}
			}
		}
		
		public event PropertyChangingEventHandler PropertyChanging;
		
		public event PropertyChangedEventHandler PropertyChanged;
		
		protected virtual void SendPropertyChanging()
		{
			if ((this.PropertyChanging != null))
			{
				this.PropertyChanging(this, emptyChangingEventArgs);
			}
		}
		
		protected virtual void SendPropertyChanged(String propertyName)
		{
			if ((this.PropertyChanged != null))
			{
				this.PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
			}
		}
	}
	
	[global::System.Data.Linq.Mapping.TableAttribute(Name="dbo.Trained_Models")]
	public partial class Trained_Model : INotifyPropertyChanging, INotifyPropertyChanged
	{
		
		private static PropertyChangingEventArgs emptyChangingEventArgs = new PropertyChangingEventArgs(String.Empty);
		
		private int _Id;
		
		private string _Model_Name;
		
		private System.Nullable<int> _Number_OF_Users;
		
		private System.Nullable<int> _Model_Version;
		
    #region Extensibility Method Definitions
    partial void OnLoaded();
    partial void OnValidate(System.Data.Linq.ChangeAction action);
    partial void OnCreated();
    partial void OnIdChanging(int value);
    partial void OnIdChanged();
    partial void OnModel_NameChanging(string value);
    partial void OnModel_NameChanged();
    partial void OnNumber_OF_UsersChanging(System.Nullable<int> value);
    partial void OnNumber_OF_UsersChanged();
    partial void OnModel_VersionChanging(System.Nullable<int> value);
    partial void OnModel_VersionChanged();
    #endregion
		
		public Trained_Model()
		{
			OnCreated();
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Id", AutoSync=AutoSync.OnInsert, DbType="Int NOT NULL IDENTITY", IsPrimaryKey=true, IsDbGenerated=true)]
		public int Id
		{
			get
			{
				return this._Id;
			}
			set
			{
				if ((this._Id != value))
				{
					this.OnIdChanging(value);
					this.SendPropertyChanging();
					this._Id = value;
					this.SendPropertyChanged("Id");
					this.OnIdChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Model_Name", DbType="NVarChar(MAX)")]
		public string Model_Name
		{
			get
			{
				return this._Model_Name;
			}
			set
			{
				if ((this._Model_Name != value))
				{
					this.OnModel_NameChanging(value);
					this.SendPropertyChanging();
					this._Model_Name = value;
					this.SendPropertyChanged("Model_Name");
					this.OnModel_NameChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Number_OF_Users", DbType="Int")]
		public System.Nullable<int> Number_OF_Users
		{
			get
			{
				return this._Number_OF_Users;
			}
			set
			{
				if ((this._Number_OF_Users != value))
				{
					this.OnNumber_OF_UsersChanging(value);
					this.SendPropertyChanging();
					this._Number_OF_Users = value;
					this.SendPropertyChanged("Number_OF_Users");
					this.OnNumber_OF_UsersChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Model_Version", DbType="Int")]
		public System.Nullable<int> Model_Version
		{
			get
			{
				return this._Model_Version;
			}
			set
			{
				if ((this._Model_Version != value))
				{
					this.OnModel_VersionChanging(value);
					this.SendPropertyChanging();
					this._Model_Version = value;
					this.SendPropertyChanged("Model_Version");
					this.OnModel_VersionChanged();
				}
			}
		}
		
		public event PropertyChangingEventHandler PropertyChanging;
		
		public event PropertyChangedEventHandler PropertyChanged;
		
		protected virtual void SendPropertyChanging()
		{
			if ((this.PropertyChanging != null))
			{
				this.PropertyChanging(this, emptyChangingEventArgs);
			}
		}
		
		protected virtual void SendPropertyChanged(String propertyName)
		{
			if ((this.PropertyChanged != null))
			{
				this.PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
			}
		}
	}
	
	[global::System.Data.Linq.Mapping.TableAttribute(Name="dbo.Trained_Models_Interaction_Data")]
	public partial class Trained_Models_Interaction_Data : INotifyPropertyChanging, INotifyPropertyChanged
	{
		
		private static PropertyChangingEventArgs emptyChangingEventArgs = new PropertyChangingEventArgs(String.Empty);
		
		private int _Id;
		
		private string _Model_Name;
		
		private System.Nullable<int> _Number_OF_Users;
		
		private System.Nullable<int> _Model_Version;
		
    #region Extensibility Method Definitions
    partial void OnLoaded();
    partial void OnValidate(System.Data.Linq.ChangeAction action);
    partial void OnCreated();
    partial void OnIdChanging(int value);
    partial void OnIdChanged();
    partial void OnModel_NameChanging(string value);
    partial void OnModel_NameChanged();
    partial void OnNumber_OF_UsersChanging(System.Nullable<int> value);
    partial void OnNumber_OF_UsersChanged();
    partial void OnModel_VersionChanging(System.Nullable<int> value);
    partial void OnModel_VersionChanged();
    #endregion
		
		public Trained_Models_Interaction_Data()
		{
			OnCreated();
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Id", AutoSync=AutoSync.OnInsert, DbType="Int NOT NULL IDENTITY", IsPrimaryKey=true, IsDbGenerated=true)]
		public int Id
		{
			get
			{
				return this._Id;
			}
			set
			{
				if ((this._Id != value))
				{
					this.OnIdChanging(value);
					this.SendPropertyChanging();
					this._Id = value;
					this.SendPropertyChanged("Id");
					this.OnIdChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Model_Name", DbType="NVarChar(MAX)")]
		public string Model_Name
		{
			get
			{
				return this._Model_Name;
			}
			set
			{
				if ((this._Model_Name != value))
				{
					this.OnModel_NameChanging(value);
					this.SendPropertyChanging();
					this._Model_Name = value;
					this.SendPropertyChanged("Model_Name");
					this.OnModel_NameChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Number_OF_Users", DbType="Int")]
		public System.Nullable<int> Number_OF_Users
		{
			get
			{
				return this._Number_OF_Users;
			}
			set
			{
				if ((this._Number_OF_Users != value))
				{
					this.OnNumber_OF_UsersChanging(value);
					this.SendPropertyChanging();
					this._Number_OF_Users = value;
					this.SendPropertyChanged("Number_OF_Users");
					this.OnNumber_OF_UsersChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Model_Version", DbType="Int")]
		public System.Nullable<int> Model_Version
		{
			get
			{
				return this._Model_Version;
			}
			set
			{
				if ((this._Model_Version != value))
				{
					this.OnModel_VersionChanging(value);
					this.SendPropertyChanging();
					this._Model_Version = value;
					this.SendPropertyChanged("Model_Version");
					this.OnModel_VersionChanged();
				}
			}
		}
		
		public event PropertyChangingEventHandler PropertyChanging;
		
		public event PropertyChangedEventHandler PropertyChanged;
		
		protected virtual void SendPropertyChanging()
		{
			if ((this.PropertyChanging != null))
			{
				this.PropertyChanging(this, emptyChangingEventArgs);
			}
		}
		
		protected virtual void SendPropertyChanged(String propertyName)
		{
			if ((this.PropertyChanged != null))
			{
				this.PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
			}
		}
	}
	
	[global::System.Data.Linq.Mapping.TableAttribute(Name="dbo.Trained_Models_Voice_Notes")]
	public partial class Trained_Models_Voice_Note : INotifyPropertyChanging, INotifyPropertyChanged
	{
		
		private static PropertyChangingEventArgs emptyChangingEventArgs = new PropertyChangingEventArgs(String.Empty);
		
		private int _Id;
		
		private string _Model_Name;
		
		private System.Nullable<int> _Number_OF_Users;
		
		private System.Nullable<int> _Model_Version;
		
    #region Extensibility Method Definitions
    partial void OnLoaded();
    partial void OnValidate(System.Data.Linq.ChangeAction action);
    partial void OnCreated();
    partial void OnIdChanging(int value);
    partial void OnIdChanged();
    partial void OnModel_NameChanging(string value);
    partial void OnModel_NameChanged();
    partial void OnNumber_OF_UsersChanging(System.Nullable<int> value);
    partial void OnNumber_OF_UsersChanged();
    partial void OnModel_VersionChanging(System.Nullable<int> value);
    partial void OnModel_VersionChanged();
    #endregion
		
		public Trained_Models_Voice_Note()
		{
			OnCreated();
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Id", AutoSync=AutoSync.OnInsert, DbType="Int NOT NULL IDENTITY", IsPrimaryKey=true, IsDbGenerated=true)]
		public int Id
		{
			get
			{
				return this._Id;
			}
			set
			{
				if ((this._Id != value))
				{
					this.OnIdChanging(value);
					this.SendPropertyChanging();
					this._Id = value;
					this.SendPropertyChanged("Id");
					this.OnIdChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Model_Name", DbType="NVarChar(MAX)")]
		public string Model_Name
		{
			get
			{
				return this._Model_Name;
			}
			set
			{
				if ((this._Model_Name != value))
				{
					this.OnModel_NameChanging(value);
					this.SendPropertyChanging();
					this._Model_Name = value;
					this.SendPropertyChanged("Model_Name");
					this.OnModel_NameChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Number_OF_Users", DbType="Int")]
		public System.Nullable<int> Number_OF_Users
		{
			get
			{
				return this._Number_OF_Users;
			}
			set
			{
				if ((this._Number_OF_Users != value))
				{
					this.OnNumber_OF_UsersChanging(value);
					this.SendPropertyChanging();
					this._Number_OF_Users = value;
					this.SendPropertyChanged("Number_OF_Users");
					this.OnNumber_OF_UsersChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Model_Version", DbType="Int")]
		public System.Nullable<int> Model_Version
		{
			get
			{
				return this._Model_Version;
			}
			set
			{
				if ((this._Model_Version != value))
				{
					this.OnModel_VersionChanging(value);
					this.SendPropertyChanging();
					this._Model_Version = value;
					this.SendPropertyChanged("Model_Version");
					this.OnModel_VersionChanged();
				}
			}
		}
		
		public event PropertyChangingEventHandler PropertyChanging;
		
		public event PropertyChangedEventHandler PropertyChanged;
		
		protected virtual void SendPropertyChanging()
		{
			if ((this.PropertyChanging != null))
			{
				this.PropertyChanging(this, emptyChangingEventArgs);
			}
		}
		
		protected virtual void SendPropertyChanged(String propertyName)
		{
			if ((this.PropertyChanged != null))
			{
				this.PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
			}
		}
	}
	
	[global::System.Data.Linq.Mapping.TableAttribute(Name="dbo.Voice_Notes")]
	public partial class Voice_Note : INotifyPropertyChanging, INotifyPropertyChanged
	{
		
		private static PropertyChangingEventArgs emptyChangingEventArgs = new PropertyChangingEventArgs(String.Empty);
		
		private int _Id;
		
		private string _Voice_Note_Path;
		
		private System.Nullable<int> _User_ID;
		
    #region Extensibility Method Definitions
    partial void OnLoaded();
    partial void OnValidate(System.Data.Linq.ChangeAction action);
    partial void OnCreated();
    partial void OnIdChanging(int value);
    partial void OnIdChanged();
    partial void OnVoice_Note_PathChanging(string value);
    partial void OnVoice_Note_PathChanged();
    partial void OnUser_IDChanging(System.Nullable<int> value);
    partial void OnUser_IDChanged();
    #endregion
		
		public Voice_Note()
		{
			OnCreated();
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Id", AutoSync=AutoSync.OnInsert, DbType="Int NOT NULL IDENTITY", IsPrimaryKey=true, IsDbGenerated=true)]
		public int Id
		{
			get
			{
				return this._Id;
			}
			set
			{
				if ((this._Id != value))
				{
					this.OnIdChanging(value);
					this.SendPropertyChanging();
					this._Id = value;
					this.SendPropertyChanged("Id");
					this.OnIdChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Voice_Note_Path", DbType="NVarChar(MAX)")]
		public string Voice_Note_Path
		{
			get
			{
				return this._Voice_Note_Path;
			}
			set
			{
				if ((this._Voice_Note_Path != value))
				{
					this.OnVoice_Note_PathChanging(value);
					this.SendPropertyChanging();
					this._Voice_Note_Path = value;
					this.SendPropertyChanged("Voice_Note_Path");
					this.OnVoice_Note_PathChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_User_ID", DbType="Int")]
		public System.Nullable<int> User_ID
		{
			get
			{
				return this._User_ID;
			}
			set
			{
				if ((this._User_ID != value))
				{
					this.OnUser_IDChanging(value);
					this.SendPropertyChanging();
					this._User_ID = value;
					this.SendPropertyChanged("User_ID");
					this.OnUser_IDChanged();
				}
			}
		}
		
		public event PropertyChangingEventHandler PropertyChanging;
		
		public event PropertyChangedEventHandler PropertyChanged;
		
		protected virtual void SendPropertyChanging()
		{
			if ((this.PropertyChanging != null))
			{
				this.PropertyChanging(this, emptyChangingEventArgs);
			}
		}
		
		protected virtual void SendPropertyChanged(String propertyName)
		{
			if ((this.PropertyChanged != null))
			{
				this.PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
			}
		}
	}
	
	[global::System.Data.Linq.Mapping.TableAttribute(Name="dbo.Users")]
	public partial class User : INotifyPropertyChanging, INotifyPropertyChanged
	{
		
		private static PropertyChangingEventArgs emptyChangingEventArgs = new PropertyChangingEventArgs(String.Empty);
		
		private int _Id;
		
		private string _User_Name;
		
		private string _Password;
		
		private string _Email;
		
		private System.Nullable<int> _Model_ID;
		
		private System.Nullable<int> _Model_ID_VN;
		
    #region Extensibility Method Definitions
    partial void OnLoaded();
    partial void OnValidate(System.Data.Linq.ChangeAction action);
    partial void OnCreated();
    partial void OnIdChanging(int value);
    partial void OnIdChanged();
    partial void OnUser_NameChanging(string value);
    partial void OnUser_NameChanged();
    partial void OnPasswordChanging(string value);
    partial void OnPasswordChanged();
    partial void OnEmailChanging(string value);
    partial void OnEmailChanged();
    partial void OnModel_IDChanging(System.Nullable<int> value);
    partial void OnModel_IDChanged();
    partial void OnModel_ID_VNChanging(System.Nullable<int> value);
    partial void OnModel_ID_VNChanged();
    #endregion
		
		public User()
		{
			OnCreated();
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Id", AutoSync=AutoSync.OnInsert, DbType="Int NOT NULL IDENTITY", IsPrimaryKey=true, IsDbGenerated=true)]
		public int Id
		{
			get
			{
				return this._Id;
			}
			set
			{
				if ((this._Id != value))
				{
					this.OnIdChanging(value);
					this.SendPropertyChanging();
					this._Id = value;
					this.SendPropertyChanged("Id");
					this.OnIdChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_User_Name", DbType="NVarChar(MAX)")]
		public string User_Name
		{
			get
			{
				return this._User_Name;
			}
			set
			{
				if ((this._User_Name != value))
				{
					this.OnUser_NameChanging(value);
					this.SendPropertyChanging();
					this._User_Name = value;
					this.SendPropertyChanged("User_Name");
					this.OnUser_NameChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Password", DbType="NVarChar(MAX)")]
		public string Password
		{
			get
			{
				return this._Password;
			}
			set
			{
				if ((this._Password != value))
				{
					this.OnPasswordChanging(value);
					this.SendPropertyChanging();
					this._Password = value;
					this.SendPropertyChanged("Password");
					this.OnPasswordChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Email", DbType="NVarChar(MAX)")]
		public string Email
		{
			get
			{
				return this._Email;
			}
			set
			{
				if ((this._Email != value))
				{
					this.OnEmailChanging(value);
					this.SendPropertyChanging();
					this._Email = value;
					this.SendPropertyChanged("Email");
					this.OnEmailChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Model_ID", DbType="Int")]
		public System.Nullable<int> Model_ID
		{
			get
			{
				return this._Model_ID;
			}
			set
			{
				if ((this._Model_ID != value))
				{
					this.OnModel_IDChanging(value);
					this.SendPropertyChanging();
					this._Model_ID = value;
					this.SendPropertyChanged("Model_ID");
					this.OnModel_IDChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Model_ID_VN", DbType="Int")]
		public System.Nullable<int> Model_ID_VN
		{
			get
			{
				return this._Model_ID_VN;
			}
			set
			{
				if ((this._Model_ID_VN != value))
				{
					this.OnModel_ID_VNChanging(value);
					this.SendPropertyChanging();
					this._Model_ID_VN = value;
					this.SendPropertyChanged("Model_ID_VN");
					this.OnModel_ID_VNChanged();
				}
			}
		}
		
		public event PropertyChangingEventHandler PropertyChanging;
		
		public event PropertyChangedEventHandler PropertyChanged;
		
		protected virtual void SendPropertyChanging()
		{
			if ((this.PropertyChanging != null))
			{
				this.PropertyChanging(this, emptyChangingEventArgs);
			}
		}
		
		protected virtual void SendPropertyChanged(String propertyName)
		{
			if ((this.PropertyChanged != null))
			{
				this.PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
			}
		}
	}
	
	[global::System.Data.Linq.Mapping.TableAttribute(Name="dbo.Accuracy_Users")]
	public partial class Accuracy_User : INotifyPropertyChanging, INotifyPropertyChanged
	{
		
		private static PropertyChangingEventArgs emptyChangingEventArgs = new PropertyChangingEventArgs(String.Empty);
		
		private int _Id;
		
		private System.Nullable<int> _User_ID;
		
		private System.Nullable<decimal> _Prediction_Accuracy_Images;
		
		private System.Nullable<decimal> _Validation_Accuracy_Images;
		
		private System.Nullable<decimal> _Prediction_Accuracy_VN;
		
		private System.Nullable<decimal> _Validation_Accuracy_VN;
		
    #region Extensibility Method Definitions
    partial void OnLoaded();
    partial void OnValidate(System.Data.Linq.ChangeAction action);
    partial void OnCreated();
    partial void OnIdChanging(int value);
    partial void OnIdChanged();
    partial void OnUser_IDChanging(System.Nullable<int> value);
    partial void OnUser_IDChanged();
    partial void OnPrediction_Accuracy_ImagesChanging(System.Nullable<decimal> value);
    partial void OnPrediction_Accuracy_ImagesChanged();
    partial void OnValidation_Accuracy_ImagesChanging(System.Nullable<decimal> value);
    partial void OnValidation_Accuracy_ImagesChanged();
    partial void OnPrediction_Accuracy_VNChanging(System.Nullable<decimal> value);
    partial void OnPrediction_Accuracy_VNChanged();
    partial void OnValidation_Accuracy_VNChanging(System.Nullable<decimal> value);
    partial void OnValidation_Accuracy_VNChanged();
    #endregion
		
		public Accuracy_User()
		{
			OnCreated();
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Id", AutoSync=AutoSync.OnInsert, DbType="Int NOT NULL IDENTITY", IsPrimaryKey=true, IsDbGenerated=true)]
		public int Id
		{
			get
			{
				return this._Id;
			}
			set
			{
				if ((this._Id != value))
				{
					this.OnIdChanging(value);
					this.SendPropertyChanging();
					this._Id = value;
					this.SendPropertyChanged("Id");
					this.OnIdChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_User_ID", DbType="Int")]
		public System.Nullable<int> User_ID
		{
			get
			{
				return this._User_ID;
			}
			set
			{
				if ((this._User_ID != value))
				{
					this.OnUser_IDChanging(value);
					this.SendPropertyChanging();
					this._User_ID = value;
					this.SendPropertyChanged("User_ID");
					this.OnUser_IDChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Prediction_Accuracy_Images", DbType="Decimal(18,0)")]
		public System.Nullable<decimal> Prediction_Accuracy_Images
		{
			get
			{
				return this._Prediction_Accuracy_Images;
			}
			set
			{
				if ((this._Prediction_Accuracy_Images != value))
				{
					this.OnPrediction_Accuracy_ImagesChanging(value);
					this.SendPropertyChanging();
					this._Prediction_Accuracy_Images = value;
					this.SendPropertyChanged("Prediction_Accuracy_Images");
					this.OnPrediction_Accuracy_ImagesChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Validation_Accuracy_Images", DbType="Decimal(18,0)")]
		public System.Nullable<decimal> Validation_Accuracy_Images
		{
			get
			{
				return this._Validation_Accuracy_Images;
			}
			set
			{
				if ((this._Validation_Accuracy_Images != value))
				{
					this.OnValidation_Accuracy_ImagesChanging(value);
					this.SendPropertyChanging();
					this._Validation_Accuracy_Images = value;
					this.SendPropertyChanged("Validation_Accuracy_Images");
					this.OnValidation_Accuracy_ImagesChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Prediction_Accuracy_VN", DbType="Decimal(18,0)")]
		public System.Nullable<decimal> Prediction_Accuracy_VN
		{
			get
			{
				return this._Prediction_Accuracy_VN;
			}
			set
			{
				if ((this._Prediction_Accuracy_VN != value))
				{
					this.OnPrediction_Accuracy_VNChanging(value);
					this.SendPropertyChanging();
					this._Prediction_Accuracy_VN = value;
					this.SendPropertyChanged("Prediction_Accuracy_VN");
					this.OnPrediction_Accuracy_VNChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Validation_Accuracy_VN", DbType="Decimal(18,0)")]
		public System.Nullable<decimal> Validation_Accuracy_VN
		{
			get
			{
				return this._Validation_Accuracy_VN;
			}
			set
			{
				if ((this._Validation_Accuracy_VN != value))
				{
					this.OnValidation_Accuracy_VNChanging(value);
					this.SendPropertyChanging();
					this._Validation_Accuracy_VN = value;
					this.SendPropertyChanged("Validation_Accuracy_VN");
					this.OnValidation_Accuracy_VNChanged();
				}
			}
		}
		
		public event PropertyChangingEventHandler PropertyChanging;
		
		public event PropertyChangedEventHandler PropertyChanged;
		
		protected virtual void SendPropertyChanging()
		{
			if ((this.PropertyChanging != null))
			{
				this.PropertyChanging(this, emptyChangingEventArgs);
			}
		}
		
		protected virtual void SendPropertyChanged(String propertyName)
		{
			if ((this.PropertyChanged != null))
			{
				this.PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
			}
		}
	}
	
	[global::System.Data.Linq.Mapping.TableAttribute(Name="dbo.Devices_Mac")]
	public partial class Devices_Mac : INotifyPropertyChanging, INotifyPropertyChanged
	{
		
		private static PropertyChangingEventArgs emptyChangingEventArgs = new PropertyChangingEventArgs(String.Empty);
		
		private int _Id;
		
		private string _Mac_Address;
		
		private System.Nullable<int> _User_ID;
		
    #region Extensibility Method Definitions
    partial void OnLoaded();
    partial void OnValidate(System.Data.Linq.ChangeAction action);
    partial void OnCreated();
    partial void OnIdChanging(int value);
    partial void OnIdChanged();
    partial void OnMac_AddressChanging(string value);
    partial void OnMac_AddressChanged();
    partial void OnUser_IDChanging(System.Nullable<int> value);
    partial void OnUser_IDChanged();
    #endregion
		
		public Devices_Mac()
		{
			OnCreated();
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Id", AutoSync=AutoSync.OnInsert, DbType="Int NOT NULL IDENTITY", IsPrimaryKey=true, IsDbGenerated=true)]
		public int Id
		{
			get
			{
				return this._Id;
			}
			set
			{
				if ((this._Id != value))
				{
					this.OnIdChanging(value);
					this.SendPropertyChanging();
					this._Id = value;
					this.SendPropertyChanged("Id");
					this.OnIdChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Mac_Address", DbType="NVarChar(50)")]
		public string Mac_Address
		{
			get
			{
				return this._Mac_Address;
			}
			set
			{
				if ((this._Mac_Address != value))
				{
					this.OnMac_AddressChanging(value);
					this.SendPropertyChanging();
					this._Mac_Address = value;
					this.SendPropertyChanged("Mac_Address");
					this.OnMac_AddressChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_User_ID", DbType="Int")]
		public System.Nullable<int> User_ID
		{
			get
			{
				return this._User_ID;
			}
			set
			{
				if ((this._User_ID != value))
				{
					this.OnUser_IDChanging(value);
					this.SendPropertyChanging();
					this._User_ID = value;
					this.SendPropertyChanged("User_ID");
					this.OnUser_IDChanged();
				}
			}
		}
		
		public event PropertyChangingEventHandler PropertyChanging;
		
		public event PropertyChangedEventHandler PropertyChanged;
		
		protected virtual void SendPropertyChanging()
		{
			if ((this.PropertyChanging != null))
			{
				this.PropertyChanging(this, emptyChangingEventArgs);
			}
		}
		
		protected virtual void SendPropertyChanged(String propertyName)
		{
			if ((this.PropertyChanged != null))
			{
				this.PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
			}
		}
	}
	
	[global::System.Data.Linq.Mapping.TableAttribute(Name="dbo.Current_Num_Images")]
	public partial class Current_Num_Image : INotifyPropertyChanging, INotifyPropertyChanged
	{
		
		private static PropertyChangingEventArgs emptyChangingEventArgs = new PropertyChangingEventArgs(String.Empty);
		
		private int _Id;
		
		private System.Nullable<int> _Number;
		
    #region Extensibility Method Definitions
    partial void OnLoaded();
    partial void OnValidate(System.Data.Linq.ChangeAction action);
    partial void OnCreated();
    partial void OnIdChanging(int value);
    partial void OnIdChanged();
    partial void OnNumberChanging(System.Nullable<int> value);
    partial void OnNumberChanged();
    #endregion
		
		public Current_Num_Image()
		{
			OnCreated();
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Id", AutoSync=AutoSync.OnInsert, DbType="Int NOT NULL IDENTITY", IsPrimaryKey=true, IsDbGenerated=true)]
		public int Id
		{
			get
			{
				return this._Id;
			}
			set
			{
				if ((this._Id != value))
				{
					this.OnIdChanging(value);
					this.SendPropertyChanging();
					this._Id = value;
					this.SendPropertyChanged("Id");
					this.OnIdChanged();
				}
			}
		}
		
		[global::System.Data.Linq.Mapping.ColumnAttribute(Storage="_Number", DbType="Int")]
		public System.Nullable<int> Number
		{
			get
			{
				return this._Number;
			}
			set
			{
				if ((this._Number != value))
				{
					this.OnNumberChanging(value);
					this.SendPropertyChanging();
					this._Number = value;
					this.SendPropertyChanged("Number");
					this.OnNumberChanged();
				}
			}
		}
		
		public event PropertyChangingEventHandler PropertyChanging;
		
		public event PropertyChangedEventHandler PropertyChanged;
		
		protected virtual void SendPropertyChanging()
		{
			if ((this.PropertyChanging != null))
			{
				this.PropertyChanging(this, emptyChangingEventArgs);
			}
		}
		
		protected virtual void SendPropertyChanged(String propertyName)
		{
			if ((this.PropertyChanged != null))
			{
				this.PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
			}
		}
	}
}
#pragma warning restore 1591
