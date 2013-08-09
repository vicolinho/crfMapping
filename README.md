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
