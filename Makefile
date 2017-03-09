SBT       ?= sbt
SBT_FLAGS ?=

.PHONY: checkstyle

checkstyle:
	$(SBT) $(SBT_FLAGS) scalastyle test:scalastyle
