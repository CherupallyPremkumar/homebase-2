import os
import re

pom_path = "/Users/premkumar/Documents/chenile-corefork/pom.xml"
with open(pom_path, "r") as f:
    content = f.read()

# Force java.version and compiler version overrides in chenile-core-parent
if "<java.version>17</java.version>" not in content:
    content = content.replace(
        "<properties>", 
        "<properties>\n        <java.version>17</java.version>\n        <maven.compiler.source>17</maven.compiler.source>\n        <maven.compiler.target>17</maven.compiler.target>"
    )

with open(pom_path, "w") as f:
    f.write(content)
print("Updated chenile-corefork/pom.xml")
