<?xml version='1.0' encoding='ISO-8859-1'?>
<xsl:stylesheet version='1.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>
<xsl:template match='/'>
<html>
<head>
	<title>Memories</title>
	<style type='text/css'>
		/*div.textnotenode, div.imagenode, div.markerlistnode*/
		div
		{
			color: green;
			border: thin solid blue;
			margin: 16px 4px 16px 4px;
		}
	</style>
    <script type='text/javascript' src='jquery.js'></script>
    <script type='text/javascript'>
        $(document).ready(function()
        {
            $(".modtime").each(function(i){
                num = $(this).text()
                num = parseInt(num)
                date = new Date()
                date.setTime(num)
                $(this).text(date.toString())
            })
        })
    </script>
</head>
<body>
Hello!
<xsl:for-each select="memories/*">
    <xsl:sort select="modtime"/>
	<div>
		<!--<b><xsl:value-of select="creationtime"/></b><br/>-->
		<!--<b><xsl:value-of select="agetime"/></b><br/>-->
		<b><span class="modtime"><xsl:value-of select="modtime"/></span></b><br/>
		<xsl:value-of select="owner"/>, <xsl:value-of select="agename"/><br/>
		<xsl:choose>
			<xsl:when test="name(.)='imagenode'">
				<xsl:value-of select="caption"/><br/>
				<xsl:element name='img'>
					<xsl:attribute name='src'>
						<xsl:value-of select="imagesha1"/>.jpg
					</xsl:attribute>
				</xsl:element>
			</xsl:when>
			<xsl:when test="name(.)='textnotenode'">
				<xsl:value-of select="title"/><br/>
				<pre><xsl:value-of select="text"/></pre>
			</xsl:when>
			<xsl:when test="name(.)='markerlistnode'">
				<xsl:value-of select="gamename"/><br/>
				<pre>
					<xsl:for-each select="marker">
						<xsl:value-of select="text"/> (<xsl:value-of select="x"/>,<xsl:value-of select="y"/>,<xsl:value-of select="z"/>)<br/>
					</xsl:for-each>
				</pre>
			</xsl:when>
		</xsl:choose>
	</div>
</xsl:for-each>
</body>
</html>
</xsl:template>
</xsl:stylesheet>