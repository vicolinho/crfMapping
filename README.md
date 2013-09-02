crfMapping
==========

This program calculates the changes for a set of CRF versions. Furthermore, it classifies the relvance of changes of modified
items. A CRF version is a excel sheet, which underlies the schema of the open clinica import schema.


Getting started
---------------
<b>Console application:</b>
<ol>
<li>Specify the paths of versions in the config.properties file</li>
<li>Run ant runDiffCalculator</li>
<li>The program will generate a test.log file with the number of equal,added, deleted and modified items of two versions.
Furthermore, it shows the modified items with the property changes.
 </li>
</ol>
<b>GUI application:</b>
<ol>
<li>run ant DiffCRFTool</li>
<li> Specify the CRF Versions with the menu item Datei/Öffnen.
<li> On the left hand side you can look at every loaded version. On the right hand side you can calculcate the differences of
all versions. The tree shows the last version with his items. Every item, which was modified has a child node,
which contains a table, which contains the change history. If you select a older item of the table,
 the application presents in a table the properties,
which were changed, the old and new values.
</ol>
