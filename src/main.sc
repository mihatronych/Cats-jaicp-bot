require: slotfilling/slotFilling.sc
  module = sys.zb-common
require: patterns.sc 
require: scripts/funcs.js

init:
    bind("postProcess", function($context){
            $context.session.lastState = $context.currentState;
        })

theme: /

    state: Start
        q!: *start
        q!: * $hello *
        q: * (cancel/stop) * || fromState = /SuggestClothes
        script:
            $temp.botName = capitalize($injector.botName)
        random:
            a: Hi! My name is {{$temp.botName}}. How can i help u to choose clothes?
            a: Good day! I'm {{$temp.botName}}. You can ask me which clotes to wear anytime
            a: Hello! This is {{$temp.botName}}. Ask me about choosing clothes.
        script:
            $response.replies = $response.replies || [];
            $response.replies.push({ 
                type: "image", 
                imageUrl: "https://images.squarespace-cdn.com/content/v1/55c8073fe4b02a74ffe18e48/1601518406356-RAN4D0OU7WO3A2XQQE12/Lofi+Cali+Girl+Meme+post.jpg?format=1000w", 
                text: "This is lo-fi girl-bot" })
        go!: /SuggestClothes
    
    
    state: SuggestClothes || modal = true
        random:
            a: Which clothes do you want to choose?
            a: Which type of clothes are you interested?
        a: {{ $request.channelType }}
        if: $request.channelType === "telegram"
            inlineButtons:
                {text: "Socks", url: "https://en.wikipedia.org/wiki/Sock"}
                {text: "Trousers", url: "https://en.wikipedia.org/wiki/Trousers"}
        else:
            buttons:
                "Socks"
                "Trousers"
        
        
        state: ChooseClothes
            q: * ($socks/$trousers) *
            script:
                $session.chosenClothes = getClothesChoice($parseTree);
                log("My log: "+getClothesChoice($parseTree))
            go!: /HowMany
        
        state: CatchAll
            event!: noMatch
            go!:/Start
            
    state: HowMany
        a: How many do you want?
        
        state: GetAmount
            q: * @duckling.number *
            script:
                $session.clothesNumber = $parseTree["_duckling.number"];
            go!: /Confirm
    
    state: Confirm
        script:
            var answer = "So, you've requested " + $session.clothesNumber + " " + $nlp.conform("clothing", $session.clothesNumber) + " of type " + $session.chosenClothes + "."
            $reactions.answer(answer);
    
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
        go!: {{$session.lastState}}
        
    state: Match
        event!: match
        a: {{$context.intent.answer}}

