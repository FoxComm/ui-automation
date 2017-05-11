#!/bin/bash
# Determines if test run was successful or not based on results of tests with severity.

#if [ "$BLOCKERS" > 0 ] || [ "$CRITICALS" > 0 ]; then
#	exit 0
#elif [ "$NORMALS" > 0 ] || [ "$MINOR" >= 10 ]; then
#	# mark build as YELLOW
#elif [ "$MINORS" < 10 ] && [ "$NORMALS" == 0 ] && [ "$CRITICALS" == 0 ] && [ "$BLOCKERS" == 0 ]; then
#	exit 1
exit 0
