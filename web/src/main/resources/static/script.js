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

    function sendForm(){
        var method = document.getElementById('method').value;
        var url = document.getElementById('url').value;
        var body = document.getElementById('body').value;
        var headers = {}; // Преобразуем данные из формы в объект Map
        var headersKeys = document.querySelectorAll('[id^="header_key"]');
        var headersValues = document.querySelectorAll('[id^="header_value"]');
        for (var i = 0; i < headersKeys.length; i++) {
            var headerName = headersKeys[i].value;
            var headerValue = headersValues[i].value;
            if (!headerValue) {
                continue; // Пропускаем данную итерацию, если headerValue равен null
            }

            // Разбиваем headerValue на список по разделителям ";" или ","
            var headerValuesList = headerValue.split(/[,;]/);

            for (var j = 0; j < headerValuesList.length; j++) {
                var value = headerValuesList[j].trim(); // Удаляем лишние пробелы по краям
                if (headerName in headers) {
                    headers[headerName].push(value);
                } else {
                    headers[headerName] = [value];
                }
            }
        }
        var parameters = {}; // Преобразуем данные из формы в объект Map
        var parametersKeys = document.querySelectorAll('[id^="parameter_key"]');
        var parametersValues = document.querySelectorAll('[id^="parameter_value"]');
        for (var i = 0; i < parametersKeys.length; i++) {
            var parameterName = parametersKeys[i].value;
            var parameterValue = parametersValues[i].value;
            parameters[parameterName] = parameterValue;
        }

        // Создаем объект JsonDto
        var jsonDto = {
            method: method,
            url: url,
            body: body,
            headers: headers,
            parameters: parameters
        };

        // Преобразуем объект в JSON
        var jsonDtoJson = JSON.stringify(jsonDto);
        console.log(jsonDtoJson);

        var url = 'http://localhost:8082/api/kafka/publish';
        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: jsonDtoJson
        })
            .then(response => {
            // Handle the response
            console.log(response);
        })
            .catch(error => {
            // Handle any errors
            console.error('Error:', error);
        });
    }