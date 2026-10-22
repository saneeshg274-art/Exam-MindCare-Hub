package com.example.data

import com.example.data.model.EducationLevel
import com.example.data.model.ExamPreference
import com.example.data.model.MeditationActivity
import com.example.data.model.Question
import com.example.data.model.QuestionPaper
import com.example.data.model.SubjectType

object PyqDatabase {

  val allQuestions: List<Question> = listOf(
    // ----------------------------------------------------
    // KERALA PSC - RENAISSANCE & HISTORY
    // ----------------------------------------------------
    Question(
      id = "kpsc_ren_01",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.RENAISSANCE,
      educationLevel = EducationLevel.TENTH,
      year = 2023,
      paperTitle = "Kerala PSC LDC (Lower Division Clerk) 2023",
      questionEn = "In which year did Sree Narayana Guru consecrate the Shiva idol at Aruvippuram?",
      questionMl = "ശ്രീനാരായണഗുരു അരുവിപ്പുറം ശിവപ്രതിഷ്ഠ നടത്തിയ വർഷം ഏത്?",
      optionsEn = listOf("1888", "1893", "1903", "1924"),
      optionsMl = listOf("1888", "1893", "1903", "1924"),
      correctIndex = 0,
      explanationEn = "In 1888, Sree Narayana Guru consecrated a stone picked from the Neyyar river at Aruvippuram, stating: 'Here is the ideal place where all people live as brothers without caste hatred or religious rivalry'.",
      explanationMl = "1888-ൽ ശിവരാത്രി നാളിൽ നെയ്യാറിൽ നിന്നുമെടുത്ത ശില അരുവിപ്പുറത്ത് പ്രതിഷ്ഠിച്ചാണ് ശ്രീനാരായണഗുരു വിപ്ലവകരമായ നവോത്ഥാന സന്ദേശം നൽകിയത്."
    ),
    Question(
      id = "kpsc_ren_02",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.RENAISSANCE,
      educationLevel = EducationLevel.PLUS_TWO,
      year = 2022,
      paperTitle = "Kerala PSC Civil Police Officer (CPO) 2022",
      questionEn = "Who organized the historic 'Villuvandi Samaram' (Bullock Cart Protest) in 1893 demanding freedom of movement on public roads?",
      questionMl = "പൊതുവഴികളിലൂടെയുള്ള സഞ്ചാരസ്വാതന്ത്ര്യത്തിനായി 1893-ൽ ചരിത്രപ്രസിദ്ധമായ 'വില്ലുവണ്ടി സമരം' നയിച്ച നവോത്ഥാന നായകൻ ആര്?",
      optionsEn = listOf("Chattampi Swamikal", "Ayyankali", "Vagbhatananda", "Pandit Karuppan"),
      optionsMl = listOf("ചട്ടമ്പിസ്വാമികൾ", "അയ്യങ്കാളി", "വാഗ്ഭടാനന്ദൻ", "പണ്ഡിറ്റ് കറുപ്പൻ"),
      correctIndex = 1,
      explanationEn = "Mahatma Ayyankali rode a bullock cart dressed in regal clothes through the public roads of Venganoor in 1893, breaking caste restrictions on walking on royal roads.",
      explanationMl = "1893-ൽ വെങ്ങാനൂരിലെ പൊതുവഴിയിലൂടെ വില്ലുവണ്ടി ഓടിച്ച് ദളിതർക്കുള്ള സഞ്ചാരവിലക്കുകളെ വെല്ലുവിളിച്ചത് മഹാത്മാ അയ്യങ്കാളിയാണ്."
    ),
    Question(
      id = "kpsc_ren_03",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.RENAISSANCE,
      educationLevel = EducationLevel.DEGREE,
      year = 2023,
      paperTitle = "Kerala PSC Degree Level Prelims 2023",
      questionEn = "Who was the author of the landmark philosophical work 'Pracheena Malayalam'?",
      questionMl = "'പ്രാചീന മലയാളം' എന്ന വിഖ്യാത കൃതി രചിച്ചത് ആര്?",
      optionsEn = listOf("Kumaran Asan", "Brahmananda Sivayogi", "Chattampi Swamikal", "K. Kelappan"),
      optionsMl = listOf("കുമാരനാശാൻ", "ബ്രഹ്മാനന്ദ ശിവയോഗി", "ചട്ടമ്പിസ്വാമികൾ", "കെ. കേളപ്പൻ"),
      correctIndex = 2,
      explanationEn = "Chattampi Swamikal authored 'Pracheena Malayalam', asserting the historical egalitarian rights of indigenous Dravidian communities against ritual caste hierarchies.",
      explanationMl = "ചട്ടമ്പിസ്വാമികളാണ് 'പ്രാചീന മലയാളം' രചിച്ചത്. വേദാധികാര നിരൂപണം, അദ്വൈത ചിന്താപദ്ധതി എന്നിവയും അദ്ദേഹത്തിന്റെ കൃതികളാണ്."
    ),
    Question(
      id = "kpsc_ren_04",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.RENAISSANCE,
      educationLevel = EducationLevel.TENTH,
      year = 2021,
      paperTitle = "Kerala PSC Village Extension Officer (VEO) 2021",
      questionEn = "The famous 'Mishra Bhojanam' (Inter-caste Dining) was organized in 1917 at Cherai by:",
      questionMl = "1917-ൽ ചെറായിയിൽ ചരിത്രപ്രസിദ്ധമായ 'മിശ്രഭോജനം' സംഘടിപ്പിച്ചത് ആര്?",
      optionsEn = listOf("Sahodaran Ayyappan", "V.T. Bhattathiripad", "A.K. Gopalan", "C. Krishnan"),
      optionsMl = listOf("സഹോദരൻ അയ്യപ്പൻ", "വി.ടി. ഭട്ടതിരിപ്പാട്", "എ.കെ. ഗോപാലൻ", "സി. കൃഷ്ണൻ"),
      correctIndex = 0,
      explanationEn = "Sahodaran Ayyappan organized the first Mishra Bhojanam in 1917 at Cherai, breaking caste orthodoxy and founding the Sahodara Prasthanam.",
      explanationMl = "1917 മെയ് 29-ന് ചെറായിയിൽ 'സഹോദര സംഘം' രൂപീകരിച്ച് മിശ്രഭോജനം നടത്തിയത് സഹോദരൻ അയ്യപ്പനാണ്."
    ),

    // ----------------------------------------------------
    // KERALA PSC - INDIAN CONSTITUTION & POLITY
    // ----------------------------------------------------
    Question(
      id = "kpsc_const_01",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.CONSTITUTION,
      educationLevel = EducationLevel.TENTH,
      year = 2023,
      paperTitle = "Kerala PSC LDC 2023",
      questionEn = "Which Article of the Indian Constitution was called the 'Heart and Soul of the Constitution' by Dr. B.R. Ambedkar?",
      questionMl = "ഡോ. ബി.ആർ. അംബേദ്കർ ഇന്ത്യൻ ഭരണഘടനയുടെ 'ഹൃദയവും ആത്മാവും' എന്ന് വിശേഷിപ്പിച്ചത് ഏത് അനുച്ഛേദത്തെയാണ്?",
      optionsEn = listOf("Article 14", "Article 19", "Article 21", "Article 32"),
      optionsMl = listOf("അനുച്ഛേദം 14", "അനുച്ഛേദം 19", "അനുച്ഛേദം 21", "അനുച്ഛേദം 32"),
      correctIndex = 3,
      explanationEn = "Article 32 grants the Right to Constitutional Remedies, empowering individuals to move the Supreme Court directly via writs (Habeas Corpus, Mandamus, Prohibition, Quo Warranto, Certiorari).",
      explanationMl = "ഭരണഘടനാപരമായ പരിഹാരം കാണാനുള്ള അവകാശം ഉറപ്പുനൽകുന്ന 32-ാം അനുച്ഛേദത്തെയാണ് അംബേദ്കർ ഭരണഘടനയുടെ ഹൃദയവും ആത്മാവും എന്ന് വിശേഷിപ്പിച്ചത്."
    ),
    Question(
      id = "kpsc_const_02",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.CONSTITUTION,
      educationLevel = EducationLevel.DEGREE,
      year = 2022,
      paperTitle = "Kerala PSC Secretariat Assistant 2022",
      questionEn = "The words 'Socialist', 'Secular' and 'Integrity' were added to the Preamble of the Indian Constitution by which Amendment Act?",
      questionMl = "'സോഷ്യലിസ്റ്റ്', 'മതേതരത്വം', 'അഖണ്ഡത' എന്നീ വാക്കുകൾ ഇന്ത്യൻ ഭരണഘടനയുടെ ആമുഖത്തിൽ ചേർത്ത ഭേദഗതി ഏത്?",
      optionsEn = listOf("42nd Amendment Act, 1976", "44th Amendment Act, 1978", "52nd Amendment Act, 1985", "73rd Amendment Act, 1992"),
      optionsMl = listOf("42-ാം ഭരണഘടനാ ഭേദഗതി, 1976", "44-ാം ഭരണഘടനാ ഭേദഗതി, 1978", "52-ാം ഭരണഘടനാ ഭേദഗതി, 1985", "73-ാം ഭരണഘടനാ ഭേദഗതി, 1992"),
      correctIndex = 0,
      explanationEn = "The 42nd Constitutional Amendment Act of 1976 (known as the Mini-Constitution) added the terms Socialist, Secular, and Integrity to the Preamble.",
      explanationMl = "1976-ലെ 42-ാം ഭരണഘടനാ ഭേദഗതിയിലൂടെയാണ് ആമുഖത്തിൽ 'സോഷ്യലിസ്റ്റ്', 'സെക്കുലർ', 'അഖണ്ഡത' എന്നീ പദങ്ങൾ ഉൾപ്പെടുത്തിയത്."
    ),
    Question(
      id = "kpsc_const_03",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.CONSTITUTION,
      educationLevel = EducationLevel.PLUS_TWO,
      year = 2023,
      paperTitle = "Kerala PSC Civil Excise Officer 2023",
      questionEn = "Fundamental Duties were incorporated into Part IV-A of the Indian Constitution on the recommendation of which committee?",
      questionMl = "ഏത് കമ്മിറ്റിയുടെ ശുപാർശപ്രകാരമാണ് മൗലിക കർത്തവ്യങ്ങൾ ഭരണഘടനയുടെ ഭാഗം IV-A യിൽ ഉൾപ്പെടുത്തിയത്?",
      optionsEn = listOf("Sarkaria Commission", "Swaran Singh Committee", "Balwant Rai Mehta Committee", "Verma Committee"),
      optionsMl = listOf("സർക്കാരിയ കമ്മീഷൻ", "സ്വരണ്‍സിംഗ് കമ്മിറ്റി", "ബൽവന്ത് റായ് മേത്ത കമ്മിറ്റി", "വർമ്മ കമ്മിറ്റി"),
      correctIndex = 1,
      explanationEn = "The Swaran Singh Committee (1976) recommended the inclusion of Fundamental Duties, which led to Article 51A via the 42nd Amendment.",
      explanationMl = "സ്വരണ്‍ സിംഗ് കമ്മിറ്റിയുടെ ശുപാർശ പ്രകാരം 1976-ലെ 42-ാം ഭേദഗതിയിലൂടെ 10 മൗലിക കർത്തവ്യങ്ങൾ ഉൾപ്പെടുത്തി. പിന്നീട് 86-ാം ഭേദഗതിയിലൂടെ പതിനൊന്നാമത്തെ കർത്തവ്യവും ചേർത്തു."
    ),

    // ----------------------------------------------------
    // KERALA PSC - GEOGRAPHY & SCIENCE
    // ----------------------------------------------------
    Question(
      id = "kpsc_geo_01",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.GEOGRAPHY,
      educationLevel = EducationLevel.TENTH,
      year = 2023,
      paperTitle = "Kerala PSC LDC 2023",
      questionEn = "Which is the highest peak in South India and the Western Ghats?",
      questionMl = "ദക്ഷിണേന്ത്യയിലെയും പശ്ചിമഘട്ടത്തിലെയും ഏറ്റവും ഉയരം കൂടിയ കൊടുമുടി ഏത്?",
      optionsEn = listOf("Doddabetta", "Anamudi", "Agasthyarkoodam", "Chembra Peak"),
      optionsMl = listOf("ദൊഡ്ഡബെട്ട", "ആനമുടി", "അഗസ്ത്യാർകൂടം", "ചെമ്പ്ര പീക്ക്"),
      correctIndex = 1,
      explanationEn = "Anamudi (2,695 metres / 8,842 ft) located in the Idukki district of Kerala (inside Eravikulam National Park) is the highest peak in peninsular India.",
      explanationMl = "കേരളത്തിലെ ഇടുക്കി ജില്ലയിൽ ഇരവികുളം ദേശീയോദ്യാനത്തിൽ സ്ഥിതി ചെയ്യുന്ന ആനമുടിയാണ് (2,695 മീറ്റർ) ദക്ഷിണേന്ത്യയിലെ ഏറ്റവും ഉയരമുള്ള കൊടുമുടി."
    ),
    Question(
      id = "kpsc_geo_02",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.GEOGRAPHY,
      educationLevel = EducationLevel.PLUS_TWO,
      year = 2022,
      paperTitle = "Kerala PSC Fireman Trainee 2022",
      questionEn = "Which river flows through the Silent Valley National Park in Kerala?",
      questionMl = "കേരളത്തിലെ സൈലന്റ് വാലി ദേശീയോദ്യാനത്തിലൂടെ ഒഴുകുന്ന പുഴ ഏത്?",
      optionsEn = listOf("Bharathapuzha", "Kunthipuzha", "Chaliyar", "Pamba"),
      optionsMl = listOf("ഭാരതപ്പുഴ", "കുന്തിപ്പുഴ", "ചാലിയാർ", "പമ്പ"),
      correctIndex = 1,
      explanationEn = "Kunthipuzha, a tributary of the Bharathapuzha, flows through the heart of Silent Valley National Park, famous for Lion-tailed macaques.",
      explanationMl = "സൈലന്റ് വാലിയിലൂടെ ഒഴുകുന്ന നിത്യഹരിത നദിയാണ് കുന്തിപ്പുഴ. സിംഹവാലൻ കുരങ്ങുകളുടെ സ്വാഭാവിക ആവാസകേന്ദ്രമാണിത്."
    ),
    Question(
      id = "kpsc_sci_01",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.SCIENCE,
      educationLevel = EducationLevel.TENTH,
      year = 2023,
      paperTitle = "Kerala PSC LGS (Last Grade Servants) 2023",
      questionEn = "Deficiency of Vitamin C leads to which disease?",
      questionMl = "വിറ്റാമിൻ സി യുടെ കുറവ് മൂലം ഉണ്ടാകുന്ന രോഗം ഏത്?",
      optionsEn = listOf("Rickets", "Beriberi", "Scurvy", "Night Blindness"),
      optionsMl = listOf("റിക്കറ്റ്‌സ് (കണ)", "ബെറിബെറി", "സ്കർവി", "മാലക്കണ്ണ്"),
      correctIndex = 2,
      explanationEn = "Vitamin C (Ascorbic acid) deficiency causes Scurvy, characterized by bleeding gums, skin spots, and poor wound healing.",
      explanationMl = "വിറ്റാമിൻ സി (അസ്കോർബിക് ആസിഡ്) യുടെ അപര്യാപ്തത മൂലം ഉണ്ടാകുന്ന രോഗമാണ് സ്കർവി. മോണയിൽ നിന്ന് ചോര വരിക പ്രധാന ലക്ഷണമാണ്."
    ),
    Question(
      id = "kpsc_sci_02",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.SCIENCE,
      educationLevel = EducationLevel.DIPLOMA,
      year = 2023,
      paperTitle = "Kerala PSC Sub Engineer / Overseer 2023",
      questionEn = "Which instrument is used to measure electrical potential difference between two points in a circuit?",
      questionMl = "ഒരു സർക്യൂട്ടിലെ രണ്ട് ബിന്ദുക്കൾ തമ്മിലുള്ള പൊട്ടൻഷ്യൽ വ്യത്യാസം അളക്കാൻ ഉപയോഗിക്കുന്ന ഉപകരണം ഏത്?",
      optionsEn = listOf("Ammeter", "Voltmeter", "Galvanometer", "Ohmmeter"),
      optionsMl = listOf("അമ്മീറ്റർ", "വോൾട്ട്മീറ്റർ", "ഗാൽവനോമീറ്റർ", "ഓംമീറ്റർ"),
      correctIndex = 1,
      explanationEn = "A voltmeter is connected in parallel to measure the electrical potential difference between two nodes in a circuit.",
      explanationMl = "ഒരു സർക്യൂട്ടിൽ സമാന്തരമായി ഘടിപ്പിച്ച് വോൾട്ടേജ് (പൊട്ടൻഷ്യൽ വ്യത്യാസം) അളക്കാൻ വോൾട്ട്മീറ്റർ ഉപയോഗിക്കുന്നു."
    ),

    // ----------------------------------------------------
    // KERALA PSC - MALAYALAM LANGUAGE & LITERATURE
    // ----------------------------------------------------
    Question(
      id = "kpsc_mal_01",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.MALAYALAM,
      educationLevel = EducationLevel.TENTH,
      year = 2023,
      paperTitle = "Kerala PSC LDC 2023",
      questionEn = "Who was the first Malayalam author to receive the prestigious Jnanpith Award?",
      questionMl = "ജ്ഞാനപീഠ പുരസ്കാരം നേടിയ ആദ്യത്തെ മലയാളി ആര്?",
      optionsEn = listOf("Thakazhi Sivasankara Pillai", "G. Sankara Kurup", "S.K. Pottekkatt", "M.T. Vasudevan Nair"),
      optionsMl = listOf("തകഴി ശിവശങ്കരപ്പിള്ള", "ജി. ശങ്കരക്കുറുപ്പ്", "എസ്.കെ. പൊറ്റെക്കാട്ട്", "എം.ടി. വാസുദേവൻ നായർ"),
      correctIndex = 1,
      explanationEn = "Mahakavi G. Sankara Kurup won the very first Jnanpith Award in 1965 for his poetry collection 'Odakkuzhal' (The Bamboo Flute).",
      explanationMl = "1965-ൽ ഭാരതത്തിലെ പ്രഥമ ജ്ഞാനപീഠ പുരസ്കാരം ലഭിച്ചത് 'ഓടക്കുഴൽ' എന്ന കവിതാസമാഹാരത്തിന് മഹാകവി ജി. ശങ്കരക്കുറുപ്പിനാണ്."
    ),
    Question(
      id = "kpsc_mal_02",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.MALAYALAM,
      educationLevel = EducationLevel.PLUS_TWO,
      year = 2022,
      paperTitle = "Kerala PSC Police Constable 2022",
      questionEn = "'കണ്ണീരും കിനാവും' ആരുടെ ആത്മകഥയാണ്? (Whose autobiography is 'Kanneerum Kinavum'?)",
      questionMl = "'കണ്ണീരും കിനാവും' ആരുടെ ആത്മകഥയാണ്?",
      optionsEn = listOf("V.T. Bhattathiripad", "P. Kesavadev", "Premji", "K.P. Kesava Menon"),
      optionsMl = listOf("വി.ടി. ഭട്ടതിരിപ്പാട്", "പി. കേശവദേവ്", "പ്രേംജി", "കെ.പി. കേശവമേനോൻ"),
      correctIndex = 0,
      explanationEn = "'Kanneerum Kinavum' is the renowned autobiographical work of social reformer V.T. Bhattathiripad depicting the transformation of Namboothiri community.",
      explanationMl = "സാമൂഹിക പരിഷ്കർത്താവായ വി.ടി. ഭട്ടതിരിപ്പാടിന്റെ വിഖ്യാതമായ ആത്മകഥയാണ് 'കണ്ണീരും കിനാവും'."
    ),

    // ----------------------------------------------------
    // KERALA PSC - MATHS & MENTAL ABILITY
    // ----------------------------------------------------
    Question(
      id = "kpsc_math_01",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.MATHS_REASONING,
      educationLevel = EducationLevel.TENTH,
      year = 2023,
      paperTitle = "Kerala PSC LDC 2023",
      questionEn = "Find the missing number in the sequence: 4, 9, 25, 49, 121, ?",
      questionMl = "ശ്രേണിയിലെ അടുത്ത സംഖ്യ കണ്ടെത്തുക: 4, 9, 25, 49, 121, ?",
      optionsEn = listOf("144", "169", "196", "225"),
      optionsMl = listOf("144", "169", "196", "225"),
      correctIndex = 1,
      explanationEn = "The sequence represents the squares of prime numbers: 2² = 4, 3² = 9, 5² = 25, 7² = 49, 11² = 121, and the next prime is 13, so 13² = 169.",
      explanationMl = "ഇത് അഭാജ്യ സംഖ്യകളുടെ (Prime numbers) വർഗ്ഗങ്ങളുടെ ശ്രേണിയാണ്: 2²=4, 3²=9, 5²=25, 7²=49, 11²=121. അടുത്ത അഭാജ്യസംഖ്യ 13 ആയതിനാൽ 13² = 169."
    ),
    Question(
      id = "kpsc_math_02",
      examPreference = ExamPreference.KERALA_PSC,
      subject = SubjectType.MATHS_REASONING,
      educationLevel = EducationLevel.DEGREE,
      year = 2023,
      paperTitle = "Kerala PSC Degree Level Prelims 2023",
      questionEn = "A train 180 metres long is moving at a uniform speed of 54 km/h. How many seconds will it take to cross an electric post?",
      questionMl = "180 മീറ്റർ നീളമുള്ള ഒരു ട്രെയിൻ 54 കി.മീ/മണിക്കൂർ വേഗതയിൽ സഞ്ചരിക്കുന്നു. ഒരു പോസ്റ്റ് കടന്നുപോകാൻ ട്രെയിനിന് എത്ര സെക്കൻഡ് വേണം?",
      optionsEn = listOf("10 seconds", "12 seconds", "14 seconds", "15 seconds"),
      optionsMl = listOf("10 സെക്കൻഡ്", "12 സെക്കൻഡ്", "14 സെക്കൻഡ്", "15 സെക്കൻഡ്"),
      correctIndex = 1,
      explanationEn = "Speed in m/s = 54 × (5/18) = 15 m/s. Time to cross post = Distance / Speed = 180 / 15 = 12 seconds.",
      explanationMl = "വേഗത m/s-ൽ = 54 × (5/18) = 15 മീ/സെക്കൻഡ്. സമയം = ദൂരം / വേഗത = 180 / 15 = 12 സെക്കൻഡ്."
    ),

    // ----------------------------------------------------
    // UPSC CIVIL SERVICES - PRELIMS PYQ
    // ----------------------------------------------------
    Question(
      id = "upsc_pol_01",
      examPreference = ExamPreference.UPSC,
      subject = SubjectType.CONSTITUTION,
      educationLevel = EducationLevel.DEGREE,
      year = 2023,
      paperTitle = "UPSC Civil Services Prelims 2023 - General Studies Paper I",
      questionEn = "In the Indian Constitution, the 'Right to Privacy' is protected as an intrinsic part of the Right to Life and Personal Liberty under which Article?",
      questionMl = "ഇന്ത്യൻ ഭരണഘടനയിൽ 'സ്വകാര്യതയ്ക്കുള്ള അവകാശം' (Right to Privacy) ജീവനും വ്യക്തിസ്വാതന്ത്ര്യത്തിനുമുള്ള അവകാശത്തിന്റെ ഭാഗമായി പരിരക്ഷിക്കപ്പെട്ടിരിക്കുന്നത് ഏത് അനുച്ഛേദപ്രകാരമാണ്?",
      optionsEn = listOf("Article 14", "Article 19", "Article 21", "Article 29"),
      optionsMl = listOf("അനുച്ഛേദം 14", "അനുച്ഛേദം 19", "അനുച്ഛേദം 21", "അനുച്ഛേദം 29"),
      correctIndex = 2,
      explanationEn = "In the landmark K.S. Puttaswamy v. Union of India (2017) judgment, a nine-judge Constitution bench unanimously held that Right to Privacy is protected under Article 21.",
      explanationMl = "കെ.എസ്. പുട്ടസ്വാമി വേഴ്സസ് യൂണിയൻ ഓഫ് ഇന്ത്യ (2017) കേസിൽ സുപ്രീം കോടതിയുടെ ഒൻപതംഗ ബെഞ്ചാണ് സ്വകാര്യതയ്ക്കുള്ള അവകാശം അനുച്ഛേദം 21 ന്റെ ഭാഗമായ മൗലികാവകാശമാണെന്ന് വിധിച്ചത്."
    ),
    Question(
      id = "upsc_pol_02",
      examPreference = ExamPreference.UPSC,
      subject = SubjectType.CONSTITUTION,
      educationLevel = EducationLevel.DEGREE,
      year = 2022,
      paperTitle = "UPSC Civil Services Prelims 2022 - General Studies Paper I",
      questionEn = "Consider the following statements regarding a Money Bill:\n1. A Money Bill can only be introduced in the Lok Sabha.\n2. The Rajya Sabha has no power to amend or reject a Money Bill.\n3. The President can return a Money Bill for reconsideration of the Parliament.\nWhich of the statements given above are correct?",
      questionMl = "ധനബിൽ (Money Bill) സംബന്ധിച്ച പ്രസ്താവനകൾ പരിഗണിക്കുക:\n1. ധനബിൽ ലോക്സഭയിൽ മാത്രമേ അവതരിപ്പിക്കാൻ സാധിക്കൂ.\n2. രാജ്യസഭയ്ക്ക് ധനബിൽ ഭേദഗതി ചെയ്യാനോ തള്ളാനോ അധികാരമില്ല.\n3. പുനഃപരിശോധനയ്ക്കായി രാഷ്ട്രപതിക്ക് ധനബിൽ തിരിച്ചയക്കാം.\nശരിയായവ ഏതെല്ലാം?",
      optionsEn = listOf("1 and 2 only", "2 and 3 only", "1 and 3 only", "1, 2 and 3"),
      optionsMl = listOf("1, 2 എന്നിവ മാത്രം", "2, 3 എന്നിവ മാത്രം", "1, 3 എന്നിവ മാത്രം", "1, 2, 3 എന്നിവയെല്ലാം"),
      correctIndex = 0,
      explanationEn = "Statements 1 and 2 are correct. Statement 3 is incorrect because under Article 111, the President cannot return a Money Bill for reconsideration as it was introduced with prior presidential recommendation.",
      explanationMl = "പ്രസ്താവന 1, 2 എന്നിവ ശരിയാണ്. രാഷ്ട്രപതിയുടെ മുൻകൂർ അനുമതിയോടെ അവതരിപ്പിക്കുന്നതിനാൽ ധനബിൽ പുനഃപരിശോധനയ്ക്ക് തിരിച്ചയക്കാൻ രാഷ്ട്രപതിക്ക് കഴിയില്ല."
    ),
    Question(
      id = "upsc_his_01",
      examPreference = ExamPreference.UPSC,
      subject = SubjectType.RENAISSANCE,
      educationLevel = EducationLevel.DEGREE,
      year = 2023,
      paperTitle = "UPSC Civil Services Prelims 2023 - General Studies Paper I",
      questionEn = "Which one of the following was a major recommendation of the Government of India Act, 1935?",
      questionMl = "1935-ലെ ഗവൺമെന്റ് ഓഫ് ഇന്ത്യ ആക്റ്റിന്റെ പ്രധാന ശുപാർശകളിൽ ഒന്ന് ഏതാണ്?",
      optionsEn = listOf(
        "Introduction of Dyarchy in the Provinces",
        "Abolition of Dyarchy in the Provinces and introduction of Provincial Autonomy",
        "Complete independence for British India",
        "Separate electorates abolished completely"
      ),
      optionsMl = listOf(
        "പ്രവിശ്യകളിൽ ദ്വിഭരണം ഏർപ്പെടുത്തി",
        "പ്രവിശ്യകളിലെ ദ്വിഭരണം നിർത്തലാക്കി പ്രവിശ്യാ സ്വയംഭരണം ഏർപ്പെടുത്തി",
        "ബ്രിട്ടീഷ് ഇന്ത്യക്ക് പൂർണ്ണ സ്വാതന്ത്ര്യം നൽകി",
        "പ്രത്യേക നിയോജകമണ്ഡലങ്ങൾ പൂർണ്ണമായി നിർത്തലാക്കി"
      ),
      correctIndex = 1,
      explanationEn = "The Government of India Act 1935 abolished provincial dyarchy, granted provincial autonomy, and proposed an All India Federation with dyarchy at the Centre.",
      explanationMl = "1935 ലെ ഗവൺമെന്റ് ഓഫ് ഇന്ത്യ ആക്ട് പ്രവിശ്യകളിലെ ദ്വിഭരണം (Dyarchy) അവസാനിപ്പിക്കുകയും പ്രവിശ്യാ സ്വയംഭരണം കൊണ്ടുവരികയും ചെയ്തു."
    ),
    Question(
      id = "upsc_eco_01",
      examPreference = ExamPreference.UPSC,
      subject = SubjectType.ECONOMY,
      educationLevel = EducationLevel.DEGREE,
      year = 2022,
      paperTitle = "UPSC Civil Services Prelims 2022 - General Studies Paper I",
      questionEn = "If the Reserve Bank of India decides to adopt an expansionist monetary policy, which of the following would it NOT do?",
      questionMl = "റിസർവ് ബാങ്ക് ഓഫ് ഇന്ത്യ (RBI) ഒരു വിപുലീകരണ പണനയം (Expansionist Monetary Policy) നടപ്പിലാക്കുമ്പോൾ താഴെ പറയുന്നവയിൽ ചെയ്യാത്തത് ഏതാണ്?",
      optionsEn = listOf(
        "Cut and optimize the Statutory Liquidity Ratio (SLR)",
        "Increase the Marginal Standing Facility (MSF) rate",
        "Cut the Repo Rate and Bank Rate",
        "Conduct Open Market Purchase of government securities"
      ),
      optionsMl = listOf(
        "സ്റ്റാറ്റ്യൂട്ടറി ലിക്വിഡിറ്റി റേഷ്യോ (SLR) കുറയ്ക്കുക",
        "മാർജിനൽ സ്റ്റാൻഡിംഗ് ഫെസിലിറ്റി (MSF) നിരക്ക് വർദ്ധിപ്പിക്കുക",
        "റീപ്പോ നിരക്കും ബാങ്ക് നിരക്കും കുറയ്ക്കുക",
        "തുറന്ന വിപണിയിൽ നിന്ന് സർക്കാർ സെക്യൂരിറ്റികൾ വാങ്ങുക"
      ),
      correctIndex = 1,
      explanationEn = "Increasing the Marginal Standing Facility (MSF) rate tightens money supply (contractionary). In an expansionist policy, the RBI eases borrowing by lowering rates and infusing liquidity.",
      explanationMl = "എം.എസ്.എഫ് നിരക്ക് വർദ്ധിപ്പിക്കുന്നത് ലിക്വിഡിറ്റി കുറയ്ക്കുകയാണ് ചെയ്യുക. വിപുലീകരണ പണനയത്തിൽ ആർ.ബി.ഐ പലിശനിരക്കുകൾ കുറച്ച് പണലഭ്യത കൂട്ടുകയാണ് ചെയ്യുന്നത്."
    ),
    Question(
      id = "upsc_csat_01",
      examPreference = ExamPreference.UPSC,
      subject = SubjectType.CSAT,
      educationLevel = EducationLevel.DEGREE,
      year = 2023,
      paperTitle = "UPSC Civil Services Prelims 2023 - CSAT Paper II",
      questionEn = "In a race of 1000 metres, A beats B by 100 metres or 10 seconds. What is the speed of A?",
      questionMl = "1000 മീറ്റർ ഓട്ടമത്സരത്തിൽ A, B യെ 100 മീറ്ററിനോ 10 സെക്കൻഡിനോ തോൽപ്പിക്കുന്നു. എങ്കിൽ A യുടെ വേഗത എത്രയാണ്?",
      optionsEn = listOf("10 m/s", "11.11 m/s", "12.5 m/s", "15 m/s"),
      optionsMl = listOf("10 m/s", "11.11 m/s", "12.5 m/s", "15 m/s"),
      correctIndex = 1,
      explanationEn = "B covers 100 m in 10 s, so Speed of B = 10 m/s. Time taken by B to run 1000 m = 100 seconds. A finishes the 1000 m race 10 s earlier, so Time for A = 90 s. Speed of A = 1000 / 90 = 11.11 m/s.",
      explanationMl = "B 100 മീറ്റർ ഓടാൻ 10 സെക്കൻഡ് എടുക്കുന്നു, അതുകൊണ്ട് B യുടെ വേഗത = 10 m/s. B 1000 മീറ്റർ ഓടാൻ എടുക്കുന്ന സമയം = 100 സെക്കൻഡ്. A 10 സെക്കൻഡ് മുമ്പ് ഫിനിഷ് ചെയ്യുന്നതിനാൽ A യുടെ സമയം = 90 സെക്കൻഡ്. വേഗത = 1000 / 90 = 11.11 m/s."
    ),

    // ----------------------------------------------------
    // SSC & BANKING / RRB TECHNICAL
    // ----------------------------------------------------
    Question(
      id = "ssc_quant_01",
      examPreference = ExamPreference.SSC_BANKING,
      subject = SubjectType.MATHS_REASONING,
      educationLevel = EducationLevel.DEGREE,
      year = 2023,
      paperTitle = "SSC CGL Tier I 2023",
      questionEn = "If 12 men can complete a work in 15 days working 8 hours a day, in how many days can 18 men complete the same work working 10 hours a day?",
      questionMl = "12 ആളുകൾ ദിവസവും 8 മണിക്കൂർ വീതം ജോലി ചെയ്താൽ 15 ദിവസം കൊണ്ട് ഒരു ജോലി പൂർത്തിയാക്കും. എങ്കിൽ 18 ആളുകൾ ദിവസവും 10 മണിക്കൂർ വീതം ജോലി ചെയ്താൽ എത്ര ദിവസം കൊണ്ട് ഈ ജോലി പൂർത്തിയാക്കും?",
      optionsEn = listOf("6 days", "8 days", "10 days", "12 days"),
      optionsMl = listOf("6 ദിവസം", "8 ദിവസം", "10 ദിവസം", "12 ദിവസം"),
      correctIndex = 1,
      explanationEn = "M1 × D1 × H1 = M2 × D2 × H2 -> 12 × 15 × 8 = 18 × D2 × 10 -> 1440 = 180 × D2 -> D2 = 8 days.",
      explanationMl = "M1 × D1 × H1 = M2 × D2 × H2 -> 12 × 15 × 8 = 18 × D2 × 10 -> 1440 = 180 × D2 -> D2 = 8 ദിവസം."
    ),
    Question(
      id = "rrb_tech_01",
      examPreference = ExamPreference.RRB_TECH,
      subject = SubjectType.SCIENCE,
      educationLevel = EducationLevel.ITI,
      year = 2023,
      paperTitle = "RRB Assistant Loco Pilot (ALP) & Technician 2023",
      questionEn = "What is the unit of Electrical Resistance in SI units?",
      questionMl = "എസ്.ഐ യൂണിറ്റിൽ വൈദ്യുത പ്രതിരോധത്തിന്റെ (Resistance) ഏകകം ഏതാണ്?",
      optionsEn = listOf("Ohm", "Watt", "Volt", "Ampere"),
      optionsMl = listOf("ഓം (Ohm)", "വാട്ട് (Watt)", "വോൾട്ട് (Volt)", "ആമ്പിയർ (Ampere)"),
      correctIndex = 0,
      explanationEn = "The SI unit of electrical resistance is Ohm (Ω), defined by Ohm's Law (V = IR).",
      explanationMl = "വൈദ്യുത പ്രതിരോധത്തിന്റെ എസ്.ഐ യൂണിറ്റ് ഓം (Ohm - Ω) ആണ്."
    )
  )

  // Full Question Papers for complete OMR tests & PDF downloads
  val questionPapers: List<QuestionPaper> = listOf(
    QuestionPaper(
      id = "paper_kpsc_ldc_2023",
      titleEn = "Kerala PSC LDC (Lower Division Clerk) 2023 Official Paper",
      titleMl = "കേരള പി.എസ്.സി എൽ.ഡി.സി 2023 ഔദ്യോഗിക ചോദ്യപേപ്പർ",
      examPreference = ExamPreference.KERALA_PSC,
      educationLevel = EducationLevel.TENTH,
      year = 2023,
      durationMinutes = 75,
      totalMarks = 100.0,
      questions = allQuestions.filter { it.examPreference == ExamPreference.KERALA_PSC }
    ),
    QuestionPaper(
      id = "paper_kpsc_degree_2023",
      titleEn = "Kerala PSC Degree Level Prelims 2023 (Sec Asst / KAS Stage I)",
      titleMl = "കേരള പി.എസ്.സി ഡിഗ്രി ലെവൽ പ്രിലിംസ് 2023 (സെക്രട്ടേറിയറ്റ് അസിസ്റ്റന്റ്)",
      examPreference = ExamPreference.KERALA_PSC,
      educationLevel = EducationLevel.DEGREE,
      year = 2023,
      durationMinutes = 90,
      totalMarks = 100.0,
      questions = allQuestions.filter { it.examPreference == ExamPreference.KERALA_PSC && (it.educationLevel == EducationLevel.DEGREE || it.educationLevel == EducationLevel.PLUS_TWO) }
    ),
    QuestionPaper(
      id = "paper_upsc_prelims_2023",
      titleEn = "UPSC Civil Services Prelims 2023 - General Studies Paper I",
      titleMl = "യു.പി.എസ്.സി സിവിൽ സർവീസസ് പ്രിലിംസ് 2023 - പേപ്പർ 1",
      examPreference = ExamPreference.UPSC,
      educationLevel = EducationLevel.DEGREE,
      year = 2023,
      durationMinutes = 120,
      totalMarks = 200.0,
      negativeMarkingPerWrong = 0.66,
      questions = allQuestions.filter { it.examPreference == ExamPreference.UPSC }
    ),
    QuestionPaper(
      id = "paper_ssc_cgl_2023",
      titleEn = "SSC CGL / Banking Mock PYQ Sprint 2023",
      titleMl = "എസ്.എസ്.സി സി.ജി.എൽ / ബാങ്കിംഗ് മാതൃകാ ചോദ്യപേപ്പർ",
      examPreference = ExamPreference.SSC_BANKING,
      educationLevel = EducationLevel.DEGREE,
      year = 2023,
      durationMinutes = 60,
      totalMarks = 50.0,
      questions = allQuestions.filter { it.examPreference == ExamPreference.SSC_BANKING || it.subject == SubjectType.MATHS_REASONING }
    ),
    QuestionPaper(
      id = "paper_rrb_tech_2023",
      titleEn = "RRB ALP & Technical Trades PYQ Paper",
      titleMl = "ആർ.ആർ.ബി അസിസ്റ്റന്റ് ലോക്കോ പൈലറ്റ് & ടെക്നീഷ്യൻ ചോദ്യപേപ്പർ",
      examPreference = ExamPreference.RRB_TECH,
      educationLevel = EducationLevel.ITI,
      year = 2023,
      durationMinutes = 60,
      totalMarks = 75.0,
      questions = allQuestions.filter { it.examPreference == ExamPreference.RRB_TECH || it.subject == SubjectType.SCIENCE }
    )
  )

  // Guided Meditation and Mind Relaxation activities
  val meditationActivities: List<MeditationActivity> = listOf(
    MeditationActivity(
      id = "med_478",
      type = com.example.data.model.MeditationExerciseType.BREATH_478,
      titleEn = "4-7-8 Relaxing Breath",
      titleMl = "4-7-8 റിലാക്സിങ് ശ്വസന വ്യായാമം",
      subtitleEn = "Instant nervous system calming technique to wipe out exam anxiety",
      subtitleMl = "പരീക്ഷാ ഉത്കണ്ഠ അകറ്റി മനസ്സിന് പെട്ടെന്ന് ശാന്തത നൽകുന്ന ശ്വസന രീതി",
      durationMinutes = 3,
      benefitsEn = "Lowers cortisol, slows racing heart rate, calms exam panic before mock tests.",
      benefitsMl = "സ്ട്രെസ് ഹോർമോൺ കുറയ്ക്കുന്നു, ഹൃദയമിടിപ്പ് നിയന്ത്രിക്കുന്നു, പരീക്ഷാ ഭയം അകറ്റുന്നു.",
      cycleInhale = 4,
      cycleHold = 7,
      cycleExhale = 8,
      affirmationEn = "My breath is steady. My mind is clear, focused, and ready.",
      affirmationMl = "എന്റെ ശ്വാസം ശാന്തമാണ്. മനസ്സ് തികച്ചും ഏകാഗ്രവും തയ്യാറുമാണ്."
    ),
    MeditationActivity(
      id = "med_box",
      type = com.example.data.model.MeditationExerciseType.BOX_BREATHING,
      titleEn = "Box Breathing (4-4-4-4)",
      titleMl = "ബോക്സ് ബ്രീത്തിംഗ് (4-4-4-4)",
      subtitleEn = "Navy SEAL & Civil Service toppers' secret to laser-sharp focus",
      subtitleMl = "മനസ്സിന് അസാധാരണ ഏകാഗ്രതയും ഉണർവും നൽകുന്ന ശാസ്ത്രീയ രീതി",
      durationMinutes = 4,
      benefitsEn = "Heightens alertness, cleanses mental fatigue, perfect before starting a study sprint.",
      benefitsMl = "പഠന ക്ഷീണം അകറ്റി ഉയർന്ന ശ്രദ്ധയും ഓർമ്മശക്തിയും നിലനിർത്തുന്നു.",
      cycleInhale = 4,
      cycleHold = 4,
      cycleExhale = 4,
      cycleHoldAfter = 4,
      affirmationEn = "I am in complete control of my thoughts and preparation.",
      affirmationMl = "എന്റെ ചിന്തകളിലും പഠനത്തിലും എനിക്ക് പൂർണ്ണ നിയന്ത്രണമുണ്ട്."
    ),
    MeditationActivity(
      id = "med_eye",
      type = com.example.data.model.MeditationExerciseType.EYE_REST_202020,
      titleEn = "20-20-20 Eye Strain Reset",
      titleMl = "20-20-20 കണ്ണ് റിലാക്സേഷൻ",
      subtitleEn = "Relieve ocular stress from hours of reading books and digital screens",
      subtitleMl = "തുടർച്ചയായ പുസ്തക വായനയും സ്ക്രീൻ കാഴ്ചയും മൂലമുണ്ടാകുന്ന കണ്ണിന്റെ ആയാസം കുറയ്ക്കുക",
      durationMinutes = 2,
      benefitsEn = "Prevents eye fatigue, headaches, and keeps reading vision crisp.",
      benefitsMl = "കണ്ണുകളുടെ പുകച്ചിലും തലവേദനയും തടഞ്ഞ് വ്യക്തമായ കാഴ്ച നിലനിർത്തുന്നു.",
      cycleInhale = 5,
      cycleHold = 10,
      cycleExhale = 5,
      affirmationEn = "Resting my eyes restores my clarity and endurance.",
      affirmationMl = "കണ്ണുകൾക്ക് വിശ്രമം നൽകുന്നത് കൂടുതൽ ഊർജ്ജസ്വലത നൽകുന്നു."
    ),
    MeditationActivity(
      id = "med_mindful",
      type = com.example.data.model.MeditationExerciseType.EXAM_MINDFULNESS,
      titleEn = "5-Min Pre-Exam Zen Meditation",
      titleMl = "5 മിനിറ്റ് പരീക്ഷാ പൂർവ്വ ധ്യാനം",
      subtitleEn = "Center yourself, dissolve fear of negative marks, and embrace confidence",
      subtitleMl = "നെഗറ്റീവ് മാർക്കിങ് ഭയം ഇല്ലാതാക്കി ആത്മവിശ്വാസം നിറയ്ക്കുന്ന ധ്യാനം",
      durationMinutes = 5,
      benefitsEn = "Aligns subconscious confidence, promotes deep cognitive stillness.",
      benefitsMl = "ആത്മവിശ്വാസം വളർത്തുന്നു, മനസ്സിന്റെ ഏകാഗ്രത വർദ്ധിപ്പിക്കുന്നു.",
      cycleInhale = 4,
      cycleHold = 4,
      cycleExhale = 6,
      affirmationEn = "I have studied with dedication. I approach each question with wisdom and patience.",
      affirmationMl = "ഞാൻ ആത്മാർത്ഥമായി പഠിച്ചിട്ടുണ്ട്. ഓരോ ചോദ്യത്തെയും വിവേകത്തോടെ നേരിടും."
    )
  )
}
