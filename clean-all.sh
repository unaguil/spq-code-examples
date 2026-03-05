#!/bin/bash

set -u

# Script to clean all Maven and Gradle projects recursively.

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

MAVEN_SUCCESS=0
MAVEN_FAIL=0
GRADLE_SUCCESS=0
GRADLE_FAIL=0

clean_maven_projects() {
    if ! command -v mvn >/dev/null 2>&1; then
        echo "Skipping Maven projects: 'mvn' command not found"
        return
    fi

    echo "Cleaning Maven projects..."
    echo ""

    while IFS= read -r pom_file; do
        project_dir="$(dirname "$pom_file")"
        echo "[MAVEN] Cleaning project: $project_dir"

        if (cd "$project_dir" && mvn -q clean); then
            echo "[MAVEN] OK: $project_dir"
            MAVEN_SUCCESS=$((MAVEN_SUCCESS + 1))
        else
            echo "[MAVEN] FAIL: $project_dir"
            MAVEN_FAIL=$((MAVEN_FAIL + 1))
        fi
        echo ""
    done < <(find . -name "pom.xml" -type f | sort)
}

clean_gradle_projects() {
    echo "Cleaning Gradle projects..."
    echo ""

    while IFS= read -r gradlew_file; do
        project_dir="$(dirname "$gradlew_file")"
        echo "[GRADLE] Cleaning project: $project_dir"

        if (cd "$project_dir" && ./gradlew -q clean); then
            echo "[GRADLE] OK: $project_dir"
            GRADLE_SUCCESS=$((GRADLE_SUCCESS + 1))
        else
            echo "[GRADLE] FAIL: $project_dir"
            GRADLE_FAIL=$((GRADLE_FAIL + 1))
        fi
        echo ""
    done < <(find . -name "gradlew" -type f | sort)
}

echo "Cleaning Maven and Gradle projects under: $SCRIPT_DIR"
echo ""

clean_maven_projects
clean_gradle_projects

echo "Summary"
echo "Maven:  success=$MAVEN_SUCCESS fail=$MAVEN_FAIL"
echo "Gradle: success=$GRADLE_SUCCESS fail=$GRADLE_FAIL"

if [ "$MAVEN_FAIL" -gt 0 ] || [ "$GRADLE_FAIL" -gt 0 ]; then
    exit 1
fi

exit 0
