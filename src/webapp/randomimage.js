var snippets = [
'<a href="http://darwin-online.org.uk/content/frameset?itemID=A120.5&viewtype=image&pageseq=4"><img src="graphics/random/1832_Stephens_A120.5_004.jpg" alt="Stephens Illustrations - Mandibulata" title="Stephens Illustrations - Mandibulata" width="177" height="285" hspace="15" vspace="15" border="0" align="right" /></a>',
'<a href="http://darwin-online.org.uk/content/frameset?itemID=A120.5&viewtype=image&pageseq=5"><img src="graphics/random/1832_Stephens_A120.5_005.jpg" alt="Stephens Illustrations - Mandibulata" title="Stephens Illustrations - Mandibulata" width="177" height="285" hspace="15" vspace="15" border="0" align="right" /></a>',
'<a href="http://darwin-online.org.uk/content/frameset?itemID=A120.5&viewtype=image&pageseq=6"><img src="graphics/random/1832_Stephens_A120.5_006.jpg" alt="Stephens Illustrations - Mandibulata" title="Stephens Illustrations - Mandibulata" width="177" height="285" hspace="15" vspace="15" border="0" align="right" /></a>',
'<a href="http://darwin-online.org.uk/content/frameset?itemID=F8.4&viewtype=image&pageseq=55"><img src="graphics/random/1838_Zoology_F8.4_055.jpg" alt="Zoology of the Beagle - Mus xanthorhinus" title="Zoology of the Beagle - Mus xanthorhinus" width="197" height="285" hspace="15" vspace="15" border="0" align="right" /></a>',
'<a href="http://darwin-online.org.uk/content/frameset?itemID=F8.4&viewtype=image&pageseq=59"><img src="graphics/random/1838_Zoology_F8.4_059.jpg" alt="Zoology of the Beagle - Mammalia" title="Zoology of the Beagle - Mammalia" width="165" height="224" hspace="15" vspace="15" border="0" align="right" /></a>',

];

function initRandomImage(elementId) {
  document.getElementById(elementId).innerHTML = snippets[Math.floor(Math.random() * snippets.length)];
}