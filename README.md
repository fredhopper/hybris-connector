# Fredhopper/SAP Hybris Connector

With the Fredhopper *Connector for SAP Hybris*, you can leverage the advanced search, navigation, and merchandising capabilities of Fredhopper in your hybris-based online marketplace.

* [System Requirements](#system-requirements)
* [Installing the Extension](#installing-the-extension)
* [Configuring the Export Logic]()
* [Configuring the Query Logic]()
* [Generating and Publishing Data]()
* [Front-End Integration]()
* [Backoffice Management]()

## System Requirements

* hybris version requirement ???
* Fredhopper version requirement ???

## Installing the Extension

1. Clone the [Fredhopper GitHub repository](https://github.com/fredhopper/hybris-connector.git) to your local machine.
1. Copy `fredhoppersearch` and `fredhoppertemplate` to the location where you store custom extensions for hybris.<br>Typically: `hybris\bin\custom`
1. Generate the extension locally.
	1. On a command prompt, navigate to `hybris\bin\platform`.
	1. Run `ant extgen` and when prompted, set `fredhoppertemplate` as the template.
1. Open `config\localextensions.xml` and register `fredhoppersearch` and the newly generated extension in the list of extensions.
	
	```
	<extensions>  
  		...  
  		<extension name="fredhoppersearch" />  
  		<extension name="<generated>" />  
	</extensions>
	```
1. Open `config\local.properties` and configure the following properties.

	Property | Description
	--- | ---
	`fh.product.catalog.name` | The name of the hybris catalog to use as the source of your data.
	`fh.product.catalog.version` | The version of the hybris catalog to use.
	`fh.data.directory` | The file path where index files should be generated before upload.
	`fh.instance.host` | The fully qualified domain name of the Fredhopper host instance.
	`fh.instance.port` | The port number to connect to.
	`fh.instance.servername` | The specific server name of your instance, as used in the path when publishing data.
	`fh.instance.username` | The username to authenticate with.
	`fh.instance.password` | The password to authenticate with.
	`fh.instance.query.url` | The Fredhopper url to query against.
	`fh.instance.universe` | The Fredhopper universe to use.
	`fh.query.maxretries` | Number of retries time for a request before giving an error.

## Configuring the Export Logic

1. Define your index attributes, using `essentialdata_createExportJobConfig.impex` as an example. For each attribute you need to define:
	* attributeId
	* Fredhopper baseType
	* name (in all languages needed)
	* value provider
	* (optional) Implement value providers if you have requirements which are not addressed by the existing providers.
1. Customize (or override) the DAOs responsible of fetching products and categories (DefaultFhCategoryDao and DefaultFhProductDao).
1. Run "ant all", update running system and trigger the cronjob "fredhopperIndexExportJob" to generate and publish your index to Fredhopper.
1. Connect to Fredhopper and verify your data has been correctly published.

## Configure the Query Logic

## Generating and Publishing Data

## Front-End Integration

## Backoffice Management