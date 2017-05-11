#!/bin/bash
# Determines if test run was successful or not based on results of tests with severity.

CRITICALS=1

if [ "$BLOCKERS" > 0 ] || [ "$CRITICALS" > 0 ] || [ "$MINORS" >= 10 ] || [ "$NORMALS" > 0 ]; then
	export EXIT_CODE=1
elif [ "$MINORS" < 10 ] || [ "$NORMALS" == 0 ] && [ "$CRITICALS" == 0 ] && [ "$BLOCKERS" == 0 ]; then
	export EXIT_CODE=0
fi
