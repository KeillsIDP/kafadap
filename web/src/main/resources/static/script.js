var headerCount = 1;
    function addHeader() {
      var newInputKey = document.createElement('input');
      newInputKey.type = 'text';
      newInputKey.id = 'header_key_'+headerCount;
      newInputKey.name = 'header_key_'+headerCount;

      var newInputValue = document.createElement('input');
      newInputValue.type = 'text';
      newInputValue.id = 'header_value_'+headerCount;
      newInputValue.name = 'header_value_'+headerCount;


      var inputsDiv = document.createElement('div');
      document.getElementById('header_fields').appendChild(inputsDiv);

      var keyLabel = document.createElement('label');
      keyLabel.htmlFor = newInputKey.id;
      keyLabel.textContent='Content-Type:';

      var valueLabel = document.createElement('label');
      valueLabel.htmlFor = newInputValue.id;
      valueLabel.textContent='Authorization:';

      var deleteButton = document.createElement('button');
      deleteButton.type = 'button';
      deleteButton.id = 'delete_header_button_'+headerCount;
      deleteButton.addEventListener('click', (e)=>{deleteHeader(deleteButton)});

      deleteButton.innerHTML='&times';
      deleteButton.classList.add("delete_button");

      inputsDiv.appendChild(keyLabel);
      inputsDiv.appendChild(newInputKey);
      inputsDiv.appendChild(valueLabel);
      inputsDiv.appendChild(newInputValue);
      inputsDiv.appendChild(deleteButton);

      headerCount++;
    }

    function deleteHeader(button) {
        var parentDiv = button.parentNode;
        parentDiv.remove();
    }

var parameterCount = 1

 function addParameter() {
      var newInputKey = document.createElement('input');
      newInputKey.type = 'text';
      newInputKey.id = 'parameter_key_'+parameterCount;
      newInputKey.name = 'parameter_key_'+parameterCount;

      var newInputValue = document.createElement('input');
      newInputValue.type = 'text';
      newInputValue.id = 'parameter_value_'+parameterCount;
      newInputValue.name = 'parameter_value_'+parameterCount;


      var inputsDiv = document.createElement('div');
      document.getElementById('parameter_fields').appendChild(inputsDiv);

      var keyLabel = document.createElement('label');
      keyLabel.htmlFor = newInputKey.id;
      keyLabel.textContent='Parameter name:';

      var valueLabel = document.createElement('label');
      valueLabel.htmlFor = newInputValue.id;
      valueLabel.textContent='Value:';

      var deleteButton = document.createElement('button');
      deleteButton.type = 'button';
      deleteButton.id = 'delete_parameter_button_'+parameterCount;
      deleteButton.addEventListener('click', (e)=>{deleteHeader(deleteButton)});

      deleteButton.innerHTML= '&times';
      deleteButton.classList.add("delete_button");

      inputsDiv.appendChild(keyLabel);
      inputsDiv.appendChild(newInputKey);
      inputsDiv.appendChild(valueLabel);
      inputsDiv.appendChild(newInputValue);
      inputsDiv.appendChild(deleteButton);

      headerCount++;
    }

    function deleteParameter(button) {
        var parentDiv = button.parentNode;
        parentDiv.remove();
    }