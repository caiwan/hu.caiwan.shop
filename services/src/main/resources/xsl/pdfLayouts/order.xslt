<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:exsl="http://exslt.org/common" xmlns:func="http://exslt.org/functions"
	xmlns:math="http://exslt.org/math" xmlns:date="http://exslt.org/dates-and-times"
	xmlns:regexp="http://exslt.org/regular-expressions"
	extension-element-prefixes="date exsl func math regexp">
	<!-- Main page layout -->
	<xsl:template match="/">
		<fo:root>

			<!-- doucument header -->
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4-portrait"
					page-height="297mm" page-width="210mm" margin-top="5mm"
					margin-bottom="5mm" margin-left="5mm" margin-right="5mm">
					<fo:region-body margin-top="25mm" margin-bottom="20mm" />
					<fo:region-before region-name="invoice-header"
						extent="25mm" display-align="before" precedence="true" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<!--/document header -->



			<!-- document content -->
			<!-- billing header -->

			<xsl:variable name="issueDate" select="order/date" />

			<fo:page-sequence master-reference="A4-portrait">
				<fo:static-content flow-name="invoice-header">
					<fo:block text-align="center" font-size="150%">Szamla/Szallitolevel
					</fo:block>
					<fo:table table-layout="fixed" width="100%" font-size="10pt"
						border-color="black" border-width="0.4mm" border-style="solid">
						<fo:table-column column-width="proportional-column-width(33)" />
						<fo:table-column column-width="proportional-column-width(33)" />
						<fo:table-column column-width="proportional-column-width(33)" />
						<fo:table-body>
							<fo:table-row>
								<fo:table-cell>
									<fo:block font-weight="bold" font-size="105%">Felado neve
									</fo:block>
									<fo:block>Felado cime</fo:block>
									<fo:block>Felado szamalszama</fo:block>
								</fo:table-cell>
								<fo:table-cell>
									<xsl:apply-templates select="order/billing" />
								</fo:table-cell>
								<fo:table-cell>
									<xsl:apply-templates select="order/shipping" />
								</fo:table-cell>
							</fo:table-row>
						</fo:table-body>
					</fo:table>

					<fo:block>
						Szamla kelte:
						<xsl:value-of select="date:formatDate($issueDate, 'YYYY-MM-DD')" />
					</fo:block>
				</fo:static-content>

				<!-- billing content -->
				<fo:flow flow-name="xsl-region-body" border-collapse="collapse"
					reference-orientation="0">
					<xsl:call-template name="ordereditems" />
				</fo:flow>

			</fo:page-sequence>
			<!--/content -->
		</fo:root>
	</xsl:template>


	<!-- address -->
	<xsl:template match="billing|shipping">
		<fo:block font-weight="bold" font-size="105%">
			<xsl:value-of select="address/name" />
		</fo:block>
		<fo:block>
			<fo:block>
				<xsl:value-of select="address/address1" />
			</fo:block>
			<fo:block>
				<xsl:value-of select="address/address2" />
			</fo:block>
			<fo:block>
				<fo:inline>
					<xsl:value-of select="address/city" />
					&#160;
				</fo:inline>
				<fo:inline>
					<xsl:value-of select="address/province" />
					,&#160;
				</fo:inline>
				<fo:inline>
					<xsl:value-of select="address/country" />
				</fo:inline>
			</fo:block>
		</fo:block>
	</xsl:template>


	<!-- List of ordered items -->
	<xsl:template name="ordereditems">

		<xsl:variable name="vat" select="25" />
		<xsl:variable name="sumNet" select="0" />
		<xsl:variable name="sumGross" select="0" />

		<fo:table table-layout="fixed" width="100%" font-size="10pt"
			border-color="black" border-width="0.35mm" border-style="solid"
			space-after="5mm">
			<fo:table-column column-width="proportional-column-width(20)" />
			<fo:table-column column-width="proportional-column-width(50)" />
			<fo:table-column column-width="proportional-column-width(25)" />
			<fo:table-column column-width="proportional-column-width(25)" />
			<fo:table-column column-width="proportional-column-width(25)" />
			<fo:table-column column-width="proportional-column-width(25)" />
			<fo:table-header font-size="90%">
				<fo:table-row height="8mm">
					<fo:table-cell>
						<fo:block font-weight="bold">Cikkszam</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold">Megnevezes, </fo:block>
						<fo:block font-weight="bold"> mennyiseg, egyseg</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold">Netto ertek</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold">Afa %</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold">Afa ertek</fo:block>
					</fo:table-cell>
					<fo:table-cell>
						<fo:block font-weight="bold">Brutto ertek</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-header>

			<fo:table-body>
				<xsl:for-each select="order/ordered-item">

					<xsl:variable name="price" select="item/price" />
					<xsl:variable name="qty" select="quantity" />
					
					<xsl:variable name="priceNet" select="$price * $qty" />
					<xsl:variable name="priceVat" select="$vat * $priceNet div 100" />
					<xsl:variable name="priceGross" select="$priceNet + $priceVat" />

					<xsl:variable name="sumNet" select="$sumNet + $priceNet" />
					<xsl:variable name="sumGross" select="$sumGross + $priceGross" />

					<fo:table-row>
						<fo:table-cell>
							<fo:block font-family="monospace" font-size="80%">
								<xsl:value-of select="item/inventoryNumber" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block>
								<xsl:value-of select="item/name" />
							</fo:block>
							<fo:block font-size="80%">
								<xsl:value-of select="$qty" /> pcs.
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block>
								<xsl:value-of select="$price" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block>
								<xsl:value-of select="$vat" />
								%
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block>
								<xsl:value-of select="$priceNet" />
							</fo:block>
						</fo:table-cell>
						<fo:table-cell>
							<fo:block>
								<xsl:value-of select="$priceGross"/>
							</fo:block>
						</fo:table-cell>
					</fo:table-row>
				</xsl:for-each>
			</fo:table-body>

		</fo:table>
	</xsl:template>

</xsl:stylesheet>