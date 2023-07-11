import './App.css';
import Header from './component/header';
import {
  Routes,
  Route
} from "react-router-dom";
import Profile from './component/profile';
import Questions from './component/questions';
import Home from './component/home';
import Footer from './component/footer';
import Editor from './component/editor';
import QuestionViewer from './component/questions/questionDetail';
import TagQuestions from './component/TagQuestions';

function App() {
  return (
    <div className="App">
      <div id="page-body">
        <Header></Header>
        <Routes>
          <Route path="/" element={<Home></Home>} />
          <Route path="/questions" element={<Questions></Questions>} />
          <Route path="/profile" element={<Profile></Profile>} />
          <Route path="/tag-questions" element={<TagQuestions></TagQuestions>} />
          <Route path="/qeditor/:questionID" element={<Editor></Editor>} />
          <Route path="/qviewer/:questionID" element={<QuestionViewer></QuestionViewer>} />
        </Routes>
      </div>
      <Footer></Footer>
    </div>
  );
}

export default App;
