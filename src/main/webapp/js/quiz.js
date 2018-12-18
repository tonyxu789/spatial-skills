(function() {
  var questions = [{
    question: "<img src='img/qs/qs-05.jpg' class='responsive' />",
   // image: "img/qs-05.jpg",
    choices: ['A', 'B', 'C', 'D', 'E'],
    correctAnswer: 2
  }, {
    question: "<img src='img/qs/qs-06.jpg' class='responsive' />",
    choices: ['A', 'B', 'C', 'D', 'E'],
    correctAnswer: 4
  }, {
    question: "<img src='img/qs/qs-07.jpg' class='responsive' />",
    choices: ['A', 'B', 'C', 'D', 'E'],
    correctAnswer: 0
  }, {
    question: "<img src='img/qs/qs-08.jpg' class='responsive' />",
    choices: ['A', 'B', 'C', 'D', 'E'],
    correctAnswer: 3
  }, {
    question: "<img src='img/qs/qs-09.jpg' class='responsive' />",
    choices: ['A', 'B', 'C', 'D', 'E'],
    correctAnswer: 4
  }];
  
  var questionCounter = 0; //Tracks question number
  var selections = []; //Array containing user choices
  var quiz = $('#quiz'); //Quiz div object
  
  // Display initial question
  displayNext();
  
  // Click handler for the 'next' button
  $('#next').on('click', function (e) {
    e.preventDefault();
    
    // Suspend click listener during fade animation
    if(quiz.is(':animated')) {        
      return false;
    }
    choose();
    
    // If no user selection, progress is stopped
    if (isNaN(selections[questionCounter])) {
      alert('Please make a selection!');
    } else {
      questionCounter++;
      displayNext();
    }
  });
  
  // Click handler for the 'prev' button
  $('#prev').on('click', function (e) {
    e.preventDefault();
    
    if(quiz.is(':animated')) {
      return false;
    }
    choose();
    questionCounter--;
    displayNext();
  });
    
  // Click handler for the 'Start Over' button
  $('#submit').on('click', function (e) {
    e.preventDefault();
    
    var formData = $(form).serialize();
	
	$.ajax({
		type: 'POST',
		url: $(form).attr('action'),
		data: formData,
		dataType: "json"
	})
    
    if(quiz.is(':animated')) {
      return false;
    }
    questionCounter = 0;
    selections = [];
    displayNext();
    $('#submit').hide();
  });
  
  // Animates buttons on hover
  $('.button').on('mouseenter', function () {
    $(this).addClass('active');
  });
  $('.button').on('mouseleave', function () {
    $(this).removeClass('active');
  });
  
  //Timer
 /* var count = 15;
  var interval = setInterval(function(){
    document.getElementById('count').innerHTML=count;
    count--;
    if (count === 0){
      clearInterval(interval);
      document.getElementById('count').innerHTML='Done';
      // or...
      alert("You're out of time!");
    }
  }, 1000);*/
  
 
  // Creates and returns the div that contains the questions and 
  // the answer selections
  function createQuestionElement(index) {
    var qElement = $('<div>', {
      id: 'question'
    });
    
    var header = $('<h2>Question ' + (index + 1) + ':</h2>');
    qElement.append(header);
    
    var question = $('<p>').append(questions[index].question);
    qElement.append(question);
    
    var radioButtons = createRadios(index);
    qElement.append(radioButtons);
    
    return qElement;
  }
  
  // Creates a list of the answer choices as radio inputs
  function createRadios(index) {
    var radioList = $('<ul>');
    var item;
    var input = '';
    for (var i = 0; i < questions[index].choices.length; i++) {
      item = $('<li>');
      input = '<input type="radio" name="answer" value=' + i + ' />';
      input += questions[index].choices[i];
      item.append(input);
      radioList.append(item);
    }
    return radioList;
  }
  
  // Reads the user selection and pushes the value to an array
  function choose() {
    selections[questionCounter] = +$('input[name="answer"]:checked').val();
  }
  
  // Displays next requested element
  function displayNext() {
    quiz.fadeOut(function() {
      $('#question').remove();
      
      if(questionCounter < questions.length){
        var nextQuestion = createQuestionElement(questionCounter);
        quiz.append(nextQuestion).fadeIn();
        if (!(isNaN(selections[questionCounter]))) {
          $('input[value='+selections[questionCounter]+']').prop('checked', true);
        }
        
        // Controls display of 'prev' button
        if(questionCounter === 1){
          $('#prev').show();
          $('#submit').hide();
        } else if(questionCounter === 0){
        	 
          $('#prev').hide();
          $('#next').show();
          $('#submit').hide();
        }
      } else if(questionCounter = questions.length) {
        var scoreElem = displayScore();
        quiz.append(scoreElem).fadeIn();
        $('#next').hide();
        $('#prev').hide();
        $('#submit').show();
       // $('#start').show();
      }
    });
  }
  
  // Computes score and returns a paragraph element to be displayed
  function displayScore() {
    var score = $('<p>',{id: 'question'});
    
    var numCorrect = 0;
    for (var i = 0; i < selections.length; i++) {
      if (selections[i] === questions[i].correctAnswer) {
        numCorrect++;
      }
    }
    score.append('You got ' + numCorrect + ' questions out of ' +
                 questions.length + ' right!!!');
    return score;
  }
})();