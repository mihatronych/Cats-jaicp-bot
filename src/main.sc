require: slotfilling/slotFilling.sc
  module = sys.zb-common
require: patterns.sc 
    
theme: /

    state: Start
        q!: *start
        q!: * $hello *
        q: * (cancel/~stop) * || fromState = /SuggestClothes
        
        random:
            a: Hi! How can i help u to choose clothes?
            a: Good day! You can ask me which clotes to wear anytime
            a: Hello! Ask me about choosing clothes.
        
        go!: /SuggestClothes
    
    state: SuggestClothes || modal = true
        random:
            a: Which clothes do you want to choose?
            a: Which type of clothes are you interested?
        buttons:
            "Socks"
            "Trousers"
        
        
        state: ChooseClothes
            q: * (Socks) *
            q: * (Trousers) *
            go!: /WhichColour
        
        
        state: LocalCatchAll
            event: noMatch
            a: You named the clothes, which we don't have at the moment. Please, can you reformulate your answer?
            go!: ..
            
            
    state: WhichColour
        a: Which colour do you want?
        
    
    state: Hello
        intent!: /hello
        a: Hello hello
        go!: /WhatsUp


    state: CatchAll || noContext = true
        event!: noMatch
        random:
            a: I do not understand. 
            a: I'm sorry, but I can't understand you. 
        random:
            a: You said: {{$request.query}}. Can you ask me differently?
            a: Please, can you ask me differently?
        
    state: Match
        event!: match
        a: {{$context.intent.answer}}