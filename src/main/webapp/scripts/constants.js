boinqApp.constant('DatasourceConstants', {
	TYPE_LOCAL_FALDO : 0,
	TYPE_REMOTE_FALDO : 1,

	TYPE_ITEMS : {
		'0' : 'Local Faldo endpoint',
		'1' :'Remote Faldo endpoint'
	},
	
	STATUS_EMPTY : 0,
	STATUS_RAW_DATA : 1,
	STATUS_LOADING : 2,
	STATUS_COMPLETE : 3,
	STATUS_ERROR : 4,
	
	STATUS_ITEMS : {
  		'0': "Empty",
  		'1': "Contains raw data",
  		'2': "Loading into endpoint",
  		'3': "Complete",
  		'4': "Error"
	}
	
});