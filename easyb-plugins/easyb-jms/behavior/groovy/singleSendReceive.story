shared_stories "springconf"

using "jms"

def inputQueue = 'some.queue'
def outputQueue = 'some.queue'

final String MESSAGE_CONTENT = 'testMessage'

scenario "We should be able to send and receive message", {
  given "we have xml defined", {
  	messageToSend = MESSAGE_CONTENT
  }	
  
  when "we send message", {
	receivedMessage = sendAndReceive(inputQueue, outputQueue, messageToSend, 3000)
  }
  
  then "messages should be equal", {
  	assert receivedMessage == MESSAGE_CONTENT  
  }
}