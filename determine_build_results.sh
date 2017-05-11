#!/bin/bash
# Determines if test run was successful or not based on results of tests with severity.

MINORS=0
NORMALS=0
CRITICALS=0
BLOCKERS=0

if [[ "$MINORS" > 9 || "$NORMALS" > 0 || "$CRITICALS" > 0 || "$BLOCKERS" > 0 ]]; then
	export EXIT_CODE=1
elif [[ "$MINORS" < 10 && "$NORMALS" = 0 && "$CRITICALS" = 0 && "$BLOCKERS" = 0 ]]; then
	export EXIT_CODE=0
fi
