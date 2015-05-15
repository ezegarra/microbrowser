@echo on
REM -------------------------------------------
REM Generate mallet output
REM -------------------------------------------
REM C:\software\mallet\mallet-2.0.7\bin\mallet import-dir --input C:\projects\research\git\microbrowser\setup\text --output data.mallet --keep-sequence --remove-stopwords

REM -------------------------------------------
REM Generate data
REM -------------------------------------------
 C:\software\mallet\mallet-2.0.7\bin\mallet train-topics --input C:\projects\research\git\microbrowser\setup\mallet\data.mallet --num-topics 100 --output-state C:\projects\research\git\microbrowser\setup\mallet\topic-state.gz --optimize-interval 20 --output-topic-keys tutorial_keys.txt --output-doc-topics C:\projects\research\git\microbrowser\setup\mallet\tutorial_composition.txt --num-iterations 2000