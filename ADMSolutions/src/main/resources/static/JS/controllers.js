(function() {

	'use strict';

	/* controllers Module */

	var moduleCntrl = angular.module('controllers', [ 'ui.bootstrap' ]);

	moduleCntrl.controller('controller', function($rootScope, $scope, $http, $state) {

		$scope.init = function() {
			$scope.x = 'initialized angular controller succeded';
		}
	});

	moduleCntrl.controller('headerCntrl', function($rootScope, $scope, $http, $state) {

		$scope.selected = function(number) {
			$scope.select = number;
		};
	});

	moduleCntrl.controller('servicesCntrl', function($rootScope, $scope, $http, $state) {

		$scope.formation_and_administraion1 = `The term "offshore" referes to jurisdictions which are known as "tax heavens" that statutorily exempt from taxation in
			their jurisdiction of registration provided that they did not undertake business with persons resident in that
			jurisdictions. Most of this jurisdictions are generally located in exotics places and islands in various places around
			the world. This was in fact the source for the term: "off-shore". Our firm, has the outstanding capabilities, know-how,
			exprience and worlwide infrastructure, that enables us to provide our clients with the fastest, most professional,
			dedicated, efficient and trusthworthy company formation services in any given offshore jurisdiction in the world.`
											
		$scope.formation_and_administraion2 = `Our firm can offer company formation and admnistration in over 50 + juridiction around the world, in America, Europe,
			Asia middlae east and Australia. `;
		
		$scope.bankAccountOperationsContent = `Our firm can offer its clients with reliable and fast bank account opening in variouse leading banks and financial
			institutions all over the world. From Europe to Asia, from New Zeeland to offshore jurisdctions, we proud in our
			outstanding relations and bonds with some of the leading internationak banks worldwide. You can find bellow refereces to
			some of the banks who cooperate with our firm and would love to provide banking services to our fine quality
			clientele.`;
			
		$scope.financialAndRegulation = `Licensing Services 
			Having accumulated a wealth of experience in servicing international companies which offer investment and financial services worldwide, K. Treppides & Co LTD can assist 
			with the following:
			- The structure and application for a license for EU and non-EU based Investment Firms, Investment Funds, Payment and Electronic Money Institutions, 
			Gaming, Corporate Service Providers, Financial Institutions, Insurance Companies and Bank’s inter alia.
			- Advise on the current framework and procedures required in each case based on our experience on the unique way each market participant operates.
			- Advise on the activation procedure, based on the applicable license conditions and legal framework requirements.
			- Expand the license, applying for a branch or a representative office, applying of a tied agent or for new licenses 
			in EU and non EU countries (such as UK, Malta, Cyprus, Russia, South Africa, Australia, Ireland, Gibraltar, Belize, BVI, Mauritius, Seychelles, 
			Cayman Islands, Bermuda, New Zealand, Vanuatu, Labuan, Singapore, Luxemburg, Hong Kong).
			
			Internal Audit Services 
			K. Treppides & Co Ltd, provides Internal Audit Services to EU and non EU based 
			regulated entities (Investment Firms, Investment Funds, Payment Institutions, Electronic Money Institutions, Municipalities and Quasi-Governmental Organizations, 
			Banks and Insurance Companies inter alia) that wish to outsource the Internal Audit Function or to have an external Internal Auditor review their procedures.
			
			Compliance Officer and Compliance Consulting Services 
			K. Treppides & Co Ltd, provides Compliance and Compliance Consulting Services to EU and non EU based 
			regulated entities (Investment Firms, Investment Funds, AIFMs, Payment Institutions, Electronic Money Institutions, inter alia) that wish 
			to outsource the Compliance Function or to have consulting services and support in relation to compliance matters. 
			
			Compliance and Compliance Consulting Services are offered to EU and on-EU based regulated entities to ensure that they are compliant with the relevant and current 
			regulatory requirements. Compliance requirements inter alia are being defined by the EU Markets in Financial Instruments Directive and Regulation (MIFID, MIFID II and MIFIR), 
			Basel II and III, Capital Requirements Directive IV and Regulation (CRDIV and CRR), the European Markets Infrastructure Regulation (EMIR), the Foreign Account Tax
			Compliance Act (FATCA), the Alternative Investment Fund Managers Directive (AIFMD) and Anti Money Laundering Legislation. 
			
			Risk Management Services 
			Having serviced numerous Investment Firms, Investment Banks, Electronic Money Institutions and Payment Institutions, K. Treppides & Co Ltd, provides dedicated 
			active Risk Management and specialized Regulatory Compliance services as follows: 
			
			- Dedicated outsourced Risk Manager officers.
			- Internal Capital Adequacy Assessment Process (ICAAP) and Supervisory Review (SREP) assistance.
			- Regulatory Risk Compliance.
			- Capital Requirements Regulation (CRR) and Directive (CRDIV).
			- European Market Infrastructure Regulation (EMIR).
			- Markets in Financial Instruments Regulation (MiFIR) and Directive (MiFID II).
			- Enterprise Risk Management Consulting.
			
			Regulation and Advisory Services 
			K. Treppides & Co LTD, provides advice regarding any regulation surrounding companies, ranging from Companies Law, Taxation Law, 
			Investment Firms Law, Investment Funds Law across EU and/or Non-EU Jurisdictions and regulations. Also, it provides advice on industry best practices 
			that can assist the market participants to be on the forefront of client service and protection. 
			
			Corporate Finance 
			K. Treppides & Co LTD, offers services in relation to Valuations of Entities, Domains, Real Estate via a variety of ways which include inter alia Discounted Cash Flow, 
			Transaction Multiples and Trading Multiples. We further advise and assist on Leveraged Buy-Outs, Mergers and Acquisitions and offer Transactional Services. 
			These include Financial, Legal, Compliance and Regulatory Due Diligence of Entities and Transactions across jurisdictions and industries.
			Advisory Services:
			K. Treppides & Co Ltd takes a client driven approach when offering advisory services to its clients. 
			Advice can be provided in setting up the optimum capital structure as well as guidelines on expanding a business by taking into consideration regulatory,
			tax rules and industry trends.`;
	
		$scope.directorShareholdersServices = ` Nominee service is an intermediary ownership and management tool, meaning the appointment to official positions in the
			company (director, president, secretary), or as a shareholder, nominee persons, both physical and legal. Nominee
			shareholder is the nominee holder of the shares. Nominee shareholder holds shares registered in its/his name in favour
			of another person, the beneficial owner, under trust-type contractual relationships. Nominee Director is the nominal
			executive body of the company acting by written and / or oral instructions of the beneficial owner. Nominee Director
			signs the document with the beneficial owner (indemnity letter), which indemnifies the nominee director from any kind of
			claims by the authorities, creditors etc. The institution of nominee directors and shareholder existed for a long time
			and has been developed in many countries, including, for example, Great Britain, Ireland, Cyprus. It is not required
			that a nominee director or a shareholder should be of the same residence: for example, a Panamanian company’s director
			may be a citizen of the Republic of Cyprus. Generally some clients ask for the nominee service in order to have their
			names in no way associated with a foreign company. There are a number of reasons for this:`

   
		$scope.directorShareholdersServicesList = `In some countries (e.g. the UK, Hong Kong), the registers of directors (and often share registers) are open, that is
			the information can be disclosed to any person who knows whom to inquire. It is not acceptable for many persons if their
			names, and sometimes the address are available for public review in connection with his/her company. According to the
			legislation of some countries (e.g. Ireland and Singapore) the directors of companies registered there must be the
			residents, while in other countries (e.g. Hong Kong) such company must have a local secretary. The relationships between
			the nominee shareholders and directors are usually executed in the following documents: Nominee director issues General
			Power of Attorney in the name of the client. This Power of Attorney (legalized by apostille) contains a provision on
			expiration of this power of attorney (usually in 1 year), and often the stipulating that the power of attorney can not
			be withdrawn (i.e., terminated) during this period. Nominee directors may issue the signed revocation letter without a
			date, and thus the nominee can always be dismissed, even retroactively. Nominee shareholders of the company produce the
			Declaration of Trust, which confirms that the nominee shareholder is only a trustee holder in favour of the beneficial
			owner, and is not entitled either to perform any assignment of rights (purchase, sale, pledge etc.) or to participate in
			the business of the company (voting at the meeting of shareholders) without written instructions of the client. In most
			cases, the main property of the company is the bank account. Any bank works (accepts payment orders) with specific
			individuals only, who signed personally the documents for opening accounts and bank signature cards and verified his/her
			identity with a valid passport. Consequently, the bank's client, that is a person authorized to manage the account, is
			the one who opened the account only. In any conflict situation the bank is first to ask the account holder.`;
	
		$scope.consoltingAndLegal= `Our expert lawyers (Barel Law offices) are ready to help with your business legal needs. David Bar-El and Co law
			offices, is a prominent and highly respected law firm, with international reputation With accompanying offshore
			companies and online international business. The law office can provide a diverse range of legal and consultancy
			services, Including preparation of legal opinions for gambling, trading and investment firms, and start ups.`;
	
		$scope.trustFormation = `Our company works directly with licensed Trust Service Providers all over the world, and offer a wide range of bespoke
			trust services to suit the requirements of our various clients. There are many types of trusts that can be set up,
			however they all follow similar principles in that a trust is a legally binding agreement which transfers the ownership
			of property from a ‘person’ (the settlor) to another ‘person’ (the trustee) whom thereafter assume the responsibility of
			managing the trust’s assets under the direction of the trust deed for the benefit of the beneficiaries. There is an
			option to appoint a protector whom usually assumes the responsibility to safeguard the trust’s assets, usually by the
			ability to veto decisions made by the trustees. There are various benefits that can be enjoyed by the operation of a
			trust, which include: Increased privacy / confidentiality Reducing the impact of inheritance taxes Asset protection
			Investment holding Property holding Estate & tax planning`;

	});
	
	moduleCntrl.controller('pricesCntrl', function($rootScope, $scope, $http, $state, $timeout, $filter, NgMap) {

		$scope.model = {};
		$scope.model.map = {};
		$scope.search = {};

		NgMap.getMap().then(function(map) {
			$scope.map = map;
			$scope.marker = map.markers[0];

			$scope.model.map = {
				center : {
					lat : $scope.countries[0].center.latitude,
					lng : $scope.countries[0].center.longitude
				},
				zoom : $scope.countries[0].zoom
			};

			$scope.selectedCountry = $scope.countries[0].name;
		});

		// America Europe Afrirca Far East and Pacific
		$scope.countries = [
		// Onshore countries
		{
			id : 1,
			name : "Israel",
			area : "Asia",
			price : "1200$",
			center : {
				latitude : 31.046051,
				longitude : 34.851612
			},
			type : "Onshore",
			zoom : 7
		}, {
			id : 2,
			name : "USA",
			area : "America",
			price : "1500$",
			center : {
				latitude : 37.090240,
				longitude : -95.712891
			},
			type : "Onshore",
			zoom : 4
		}, {
			id : 3,
			name : "UK",
			area : "Europe",
			price : "1800$",
			center : {
				latitude : 55.378051,
				longitude : -3.435973
			},
			type : "Onshore",
			zoom : 5
		}, {
			id : 5,
			name : "France",
			area : "Europe",
			price : "1300$",
			center : {
				latitude : 46.227638,
				longitude : 2.213749
			},
			type : "Onshore",
			zoom : 5
		},
		// Offshore countries
		{
			id : 5,
			name : "Saint Vin. & the Grenad.",
			area : "America",
			price : "1300$",
			center : {
				latitude : 13.252818,
				longitude : -61.197163
			},
			type : "Offshore",
			zoom : 8
		}, {
			id : 5,
			name : "Marshal Islands",
			area : "America",
			price : "1300$",
			center : {
				latitude : 11.324691,
				longitude : 166.841742
			},
			type : "Offshore",
			zoom : 9
		}, {
			id : 5,
			name : "Dominica",
			area : "America",
			price : "1300$",
			center : {
				latitude : 15.414999,
				longitude : -61.370976
			},
			type : "Offshore",
			zoom : 9
		}, {
			id : 5,
			name : "Belize",
			area : "America",
			price : "1300$",
			center : {
				latitude : 17.189877,
				longitude : -88.497650
			},
			type : "Offshore",
			zoom : 6
		}, {
			id : 5,
			name : "Curacao",
			area : "America",
			price : "1300$",
			center : {
				latitude : 12.169570,
				longitude : -68.990020
			},
			type : "Offshore",
			zoom : 9
		}, {
			id : 5,
			name : "Panama",
			area : "America",
			price : "1300$",
			center : {
				latitude : 8.537981,
				longitude : -80.782127
			},
			type : "Offshore",
			zoom : 6
		}, {
			id : 5,
			name : "Costa Rica",
			area : "America",
			price : "1300$",
			center : {
				latitude : 9.748917,
				longitude : -83.753428
			},
			type : "Offshore",
			zoom : 6
		}, {
			id : 5,
			name : "Anguilla",
			area : "America",
			price : "1300$",
			center : {
				latitude : 18.220554,
				longitude : -63.068615
			},
			type : "Offshore",
			zoom : 8
		}, {
			id : 5,
			name : "Seychelles",
			area : "Africa",
			price : "1300$",
			center : {
				latitude : -4.679574,
				longitude : 55.491977
			},
			type : "Offshore",
			zoom : 8
		}, {
			id : 5,
			name : "British Virgin Islands",
			area : "America",
			price : "1300$",
			center : {
				latitude : 18.420695,
				longitude : -64.639968
			},
			type : "Offshore",
			zoom : 7
		}, {
			id : 5,
			name : "Vanuatu",
			area : "Far East and Pacific",
			price : "1300$",
			center : {
				latitude : -15.376706,
				longitude : 166.959158
			},
			type : "Offshore",
			zoom : 7
		} ];

		/*
		 * Cyprus United Kingdom Scotland Switzerland Ireland Malta Gibraltar Bulgaria Czech Republic Latvia Estonia
		 * Albania Serbia Montenegro Georgia Russia Poland
		 * 
		 * Far East and Pacific
		 * 
		 * Hong Kong Singapore New Zeeland U.A.E Israel
		 * 
		 */

		// TODO: change to types
		$scope.shores = [ {
			id : 0,
			value : "ALL"
		}, {
			id : 1,
			value : "Onshore"
		}, {
			id : 2,
			value : "Offshore"
		} ];

		$scope.areas = [ {
			value : "ALL",
			id : "0"
		}, {
			value : "America",
			id : "1"
		}, {
			value : "Europe",
			id : "2"
		}, {
			value : "Afrirca",
			id : "3"
		}, {
			value : "Far East and Pacific",
			id : "4"
		}, {
			value : "Asia",
			id : "5"
		} ];

		$scope.amounts = [ {
			value : "ALL",
			id : 0
		}, {
			value : "1000$",
			id : 1
		}, {
			value : "1500$",
			id : 2
		} ];

		$scope.search.area = "ALL";
		$scope.search.amount = "ALL";
		$scope.search.isShore = "ALL";

		$scope.filterdCountries = $scope.countries;

		$scope.countryClick = function(country) {
			$scope.model.map.center = {
				lat : country.center.latitude,
				lng : country.center.longitude
			}

			$scope.selectedCountry = country.name;
			$scope.map.setCenter($scope.model.map.center);
			$scope.map.setZoom(country.zoom);
			$scope.marker.setPosition($scope.model.map.center);
		};

		$scope.filterCountries = function(country) {
			if ($scope.search.area == "ALL" && $scope.search.amount == "ALL" && $scope.search.isShore == "ALL") {
				return true;
			} else if ($scope.search.area != "ALL" && $scope.search.amount != "ALL" && $scope.search.isShore != "ALL") {
				if (country.area == $scope.search.area && country.price <= $scope.search.amount
						&& country.type == $scope.search.isShore) {
					return true;
				}
			} else if ($scope.search.area != "ALL" && $scope.search.amount == "ALL" && $scope.search.isShore == "ALL") {
				if (country.area == $scope.search.area) {
					return true;
				}
			} else if ($scope.search.area == "ALL" && $scope.search.amount == "ALL" && $scope.search.isShore != "ALL") {
				if (country.type == $scope.search.isShore) {
					return true;
				}
			} else if ($scope.search.area == "ALL" && $scope.search.amount != "ALL" && $scope.search.isShore == "ALL") {
				if (country.price <= $scope.search.amount) {
					return true;
				}
			} else if ($scope.search.area != "ALL" && $scope.search.amount == "ALL" && $scope.search.isShore != "ALL") {
				if (country.type == $scope.search.isShore && country.area == $scope.search.area) {
					return true;
				}
			} else if ($scope.search.area != "ALL" && $scope.search.amount != "ALL" && $scope.search.isShore == "ALL") {
				if (country.area == $scope.search.area && country.price <= $scope.search.amount) {
					return true;
				}
			} else if ($scope.search.area != "ALL" && $scope.search.amount == "ALL" && $scope.search.isShore != "ALL") {
				if (country.area == $scope.search.area && country.type <= $scope.search.isShore) {
					return true;
				}
			} else if ($scope.search.area == "ALL" && $scope.search.amount != "ALL" && $scope.search.isShore != "ALL") {
				if (country.type == $scope.search.isShore && country.price <= $scope.search.amount) {
					return true;
				}
			}

			return false;
		}

	});

	moduleCntrl.controller('homebodyCntrl', function($rootScope, $scope, $http, $state) {

		$scope.choosingUsText = `Our company gives a comprehensive range of consulting,
								legal services, accounting and financial advisory to many clients from all over the world. We are committed to
								offering high-standard customer support with professional guidance and to building long lasting transparent
								rapports with our clients. Each client is treated with ultimate confidentiality and we are apt to serve all
								kinds of businesses, from start-up entrepreneurs to large multinational organisations, funds and regulated
								organisations. Viewed as a Global Business Partner by our clients, our unparalleled professional standards,
								personal attention and a business ideology that prioritises meeting our clients needs, enables us to deliver
								first-class levels of performance. Our international experience, customer oriented service and ambition to
								succeed, together with our extensive knowledge of local regulations, give our Clients the benefit, both Locally
								and Globally.`;
		
		$scope.serviceBox = [ {
			imagePath : "images/city.jpg",
			imageText : "Company formation and administration",
			textCss : "text-center text-15 text-bold",
			middleCss : "middle",
			state: "formation-and-administraion"
		}, {
			imagePath : "images/shake.jpg",
			imageText : "Trust formation and administration",
			textCss : "text-center text-15 text-bold",
			middleCss : "middle",
			state:"trust-formation"
		}, {
			imagePath : "images/consulting.jpg",
			imageText : "Consoulting Business Operations, Legal Services",
			textCss : "text-center text-15 text-bold",
			middleCss : "middle2",
			state:"consolting-and-legal"
		}, {
			imagePath : "images/shareholders2.jpg",
			imageText : "Dierctors Shareholders Services",
			textCss : "text-center text-15 text-bold",
			middleCss : "middle2 width-350",
			state:"director-shareholders-services"
		}, {
			imagePath : "images/financial.jpg",
			imageText : "Financial,regulation and advisory",
			textCss : "text-center text-15 text-bold",
			middleCss : "middle2",
			state:"financial-and-regulation"
		}, {
			imagePath : "images/account3.jpg",
			imageText : "Bank Account Operations",
			textCss : "text-center text-15 text-bold",
			middleCss : "middle2 width-250",
			state: "bank-account-operations"
		} ];
	});

	moduleCntrl.controller('aboutCntrl', function($rootScope, $scope, $http, $state) {
		
		$scope.aboutContext = `Our company gives a comprehensive range of consulting,
			legal services, accounting and financial advisory to many clients from all over the world. We are committed to
			offering high-standard customer support with professional guidance and to building long lasting transparent
			rapports with our clients. Each client is treated with ultimate confidentiality and we are apt to serve all
			kinds of businesses, from start-up entrepreneurs to large multinational organisations, funds and regulated
			organisations. Viewed as a Global Business Partner by our clients, our unparalleled professional standards,
			personal attention and a business ideology that prioritises meeting our clients needs, enables us to deliver
			first-class levels of performance. Our international experience, customer oriented service and ambition to
			succeed, together with our extensive knowledge of local regulations, give our Clients the benefit, both Locally
			and Globally.`;
	});

	moduleCntrl.controller('footerCntrl', function($rootScope, $scope, $http, $state) {
	});

	moduleCntrl.controller('contactCntrl', function($rootScope, $scope, $http, $state) {

		$scope.send = function() {
			alert('sent to email');
		}
	});

	moduleCntrl.controller('activitiesCntrl', function($rootScope, $scope, $http, $state) {

		$scope.add = function() {
			$scope.activity = "activity";
		};

		$scope.check = $rootScope.check;
	});

})();