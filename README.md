# Fredhopper/SAP Hybris Connector

With the Fredhopper *Connector for SAP Hybris*, you can leverage the advanced search, navigation, and merchandising capabilities of Fredhopper in your hybris-based online marketplace.

* [System Requirements]()
* [Installing the Plugin]()
* [Configuring the Export Logic]()
* [Configuring the Query Logic]()
* [Generating and Publishing Data]()
* [Front-End Integration]()
* [Backoffice Management]()

## System Requirements

* hybris version requirement ???
* Fredhopper version requirement ???

## Installing the Plugin

1. Clone the [Fredhopper GitHub repository](https://github.com/fredhopper/hybris-connector.git) to your local machine.
1. Copy `fredhoppersearch` and `fredhoppertemplate` to the location where you store custom extensions for your hybris platform.<br>Typically: `hybris\bin\custom`
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
1. Open `config\local.properties` and configure the required properties.<br>For a complete property reference, see [Hybris Connector Wiki: Add and configure related properties](https://github.com/fredhopper/hybris-connector).

## Configuring the Export Logic



## Configure the Query Logic

## Generating and Publishing Data

## Front-End Integration

## Backoffice Management