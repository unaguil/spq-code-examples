#!/bin/bash

# Script to clean all Maven projects recursively

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "Cleaning all Maven projects in $SCRIPT_DIR..."
echo ""

# Find all pom.xml files recursively and clean projects
while IFS= read -r pom_file; do
    project_dir="$(dirname "$pom_file")"
    echo "Cleaning project: $project_dir"
    (cd "$project_dir" && mvn clean)
    if [ $? -eq 0 ]; then
        echo "✓ Successfully cleaned: $project_dir"
    else
        echo "✗ Failed to clean: $project_dir"
    fi
    echo ""
done < <(find . -name "pom.xml" -type f)

echo "All projects cleaned!"
