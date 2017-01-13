# Fredhopper/SAP Hybris Connector

*Powerful on-site search, navigation, personalisation and merchandising, tailored to the specific needs of leading retailers*

![](Fredhopper_logo.jpg)

With the *Fredhopper/SAP Hybris Connector*, you can leverage the advanced on-site search, navigation, personalisation, and merchandising capabilities of Fredhopper in your hybris-based online marketplace.

* [System Requirements](#system-requirements)
* [Installation and Configuration](#installation-and configuration)
	* [Install the Extension](#install-the-extension)
	* [Configure the Export Logic](#configure-the-export-logic)
	* [Configure the Query Logic](#configure-the-query-logic)
* [License](#license)

## System Requirements

* SAP Hybris 6.x
* Configured Fredhopper instance<br>The machine on which you are running hybris must be able to reach the Fredhopper instance.

## Installation and Configuration

To be able to use the Fredhopper/SAP Hyris connector, you need to build and install the extension locally and configure the export and query logic.

### Install the Extension

Before you can use the Fredhopper/SAP Hybris connector, you need to build the custom extension locally and add it to hybris.

1. Clone the [Fredhopper/SAP Hybris Connector repository](https://github.com/fredhopper/hybris-connector.git) to your local machine.
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

### Configure the Export Logic

After you have installed the connector locally, you need to export your data from hybris and load it into Fredhopper.

1. Define your index attributes, using `essentialdata_createExportJobConfig.impex` as an example. For each attribute, you need to define the following:

	Attribute Meta Data | Type | Description
	---|---|---
	`attributeId` | string | An identifier for the attribute.
	`baseType` | string | The Fredhopper basetype. For a list of the valid values, see [Understand Fredhopper data types (basetypes)](https://www.fredhopper.com/learningcenter/x/Pomx).
	`name` | string | The attribute name. You need to specify it for all required languages.
	`provider` | string | A Spring bean identifier which acts as the value provider for the attribute. 
	
	> NOTE: You can implement additional value providers if you have requirements that are not addressed by the existing providers.
1. Customize or override `DefaultFhCategoryDao` and `DefaultFhProductDao`.<br>These data access objects are responsible for fetching categories and products, respectively.
1. To generate and publish your index to Fredhopper, run `ant all`, update the running system, and trigger the cron job `fredhopperIndexExportJob`.
1. Connect to Fredhopper and verify your data has been correctly published.

For more information, see [Generating and Publishing Data](https://github.com/fredhopper/hybris-connector/wiki/Generating-and-publishing-data).

> **TIP:** You can also manage key configuration items from the hybris backoffice. For more information, see [Backoffice Management](https://github.com/fredhopper/hybris-connector/wiki/Back-office-management).

### Configure the Query Logic

After you have loaded your data in Fredhopper, you can configure the way hybris queries Fredhoppers.

1. Customize or override the `DocumentSearchResultValuePopulator` class.<br>The class is responsible at service level for populating the `DocumentData.values` attribute.
1. Run `ant all`, access your front-end, and submit a search request. Your results should be coming from Hybris.

For more information, see [Front-End Integration](https://github.com/fredhopper/hybris-connector/wiki/Front-end-integration).

## License

This software is licensed under the Apache 2.0 license, quoted <a href="LICENSE" target="_blank">here</a>.