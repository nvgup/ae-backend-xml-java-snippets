# AgileEngine backend-XML java snippets

You should use java -jar <your_bundled_app>.jar <input_origin_file_path> <input_other_sample_file_path> <target_element_id> to execute a program.
<target_element_id> is an optional parameter and could be omitted. In this case 'make-everything-ok-button' would be used

Outputs example: html[0] -> body[1] -> div[2] -> ..
The index in brackets means the element position in the parent element, starting from 0