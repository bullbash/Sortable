# Sortable
Challenge

Here is a suggested solution for the Sortable.com coding challenge:
I employ a Java implementation of simplified *perceptron* with single layer of receptors.
The idea is to create a set of those receptors (and such to define the neural network topology) 
by using the UI parameters: 
"Dendrite Size" - maximum number of consecutive symbols *felt* by a single neuron's dendrite
"Dendrites Number" - a number of dendrites of a neuron (a number of symbols' sequences) contributing to a neuron excitement
"Dendrites Spread" - a max number of symbols apart between a neuron dendrtites
Note: it all could be described without fancy NN terminology - in terms of just symbols sequences.

Button "Start" starts the comparison of a <Product Name> to each <Listing name> from given files in <Home directory>.
Comparison measure is calculated in processors.Worker.getMetrics() method.
Currently it calculates a number of equal symbol sequences for given <Product:product_name> and <Listing:title> only.
Leaves *a lot* of room for fine tuning.

Suggested solution is a cut out from multi-layered NLP social news parser working with *much* longer pieces. 
I make no assumption on the nature of studied sequences - could be string of any human language as well as
market behavior, digitized sounds and such.

Test runs showed:
There is a general tendency to increase classification *precision* by increasing the NN topology complexity, with
increase of processing time.

Th resulting file in <Home directory> is results[topology].txt file. I slightly changed requested output format to
better illustrate *similarity metric*  relevancy of different NN topologies and reduce the output file size.

Could be a start for further improvements.
