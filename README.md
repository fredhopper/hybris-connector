# Fredhopper/SAP Hybris Connector

With the Fredhopper *Connector for SAP Hybris*, you can leverage the advanced search, navigation, and merchandising capabilities of Fredhopper in your hybris-based online marketplace.

* [System Requirements](#system-requirements)
* [Install the Extension](#install-the-extension)
* [Next Steps](#next-steps)

## System Requirements

* SAP Hybris 6.x
* Configured Fredhopper instance<br>The machine on which you are running hybris must be able to reach the Fredhopper instance.

## Install the Extension

Before you can use the Fredhopper/SAP Hybris connector, you need to build the custom extension locally and add it to hybris.

1. Clone the [Fredhopper GitHub repository](https://github.com/fredhopper/hybris-connector.git) to your local machine.
1. Copy `fredhoppersearch` and `fredhoppertemplate` to the hybris location for user-defined extensions.<br>Typically: `hybris/bin/custom`
1. Generate the extension locally.
	1. On a command prompt, navigate to `hybris/bin/platform`.
	1. Run `ant extgen` and when prompted, set `fredhoppertemplate` as the template.
1. Open `config/localextensions.xml` and register `fredhoppersearch` and the newly generated extension in the list of extensions.
	
	```
	<extensions>  
  		...  
  		<extension name="fredhoppersearch" />  
  		<extension name="<generated>" />  
	</extensions>
	```
1. Open `config/local.properties` and configure the following properties.

	Property | Description
	--- | ---
	`fh.product.catalog.name` | The name of the hybris catalogue to use as the source of your data.
	`fh.product.catalog.version` | The version of the hybris catalogue to use.
	`fh.data.directory` | The file path where the index files will be generated before upload.
	`fh.instance.host` | The fully qualified domain name of the Fredhopper host instance.
	`fh.instance.port` | The port number of the Fredhopper instance to which to connect.
	`fh.instance.servername` | The specific server name of your Fredhopper instance, as used in the path when publishing data.
	`fh.instance.username` | The username to authenticate with Fredhopper.
	`fh.instance.password` | The password to authenticate with Fredhopper.
	`fh.instance.query.url` | The Fredhopper URL to query against.
	`fh.instance.universe` | The Fredhopper universe to use.
	`fh.query.maxretries` | Number of retries for any request before returning an error.
1. Rebuild hybris.

## Next Steps

After you have installed the connector, you need to complete the following steps:

1. Configure the export logic.
1. Configure the query logic.
1. Generate and publish data.
1. Configure the front-end.

## Configuring the Export Logic

1. Define your index attributes, using `essentialdata_createExportJobConfig.impex` as an example. For each attribute, you need to define the following:

	Attribute Meta Data | Type | Description
	---|---|---
	`attributeId` | string | An identifier for the attribute.
	`baseType` | string | The Fredhopper basetype. For a list of the valid values, see [Understand Fredhopper data types (basetypes)](https://www.fredhopper.com/learningcenter/x/Pomx).
	`name` | string | The attribute name. You need to specify it for all required languages.
	`provider` | string | A Spring bean identifier which acts as the value provider for the attribute. 
	
	> NOTE: You can implement additional value providers if you have requirements which are not addressed by the existing providers.
1. Customize or override the DAOs responsible of fetching products and categories (`DefaultFhCategoryDao` and `DefaultFhProductDao`).
1. Run "ant all", update running system and trigger the cron job "fredhopperIndexExportJob" to generate and publish your index to Fredhopper.
1. Connect to Fredhopper and verify your data has been correctly published.

## Configure the Query Logic

## Generating and Publishing Data

## Front-End Integration

## Backoffice Management