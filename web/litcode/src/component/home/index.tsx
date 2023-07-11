import './index.css'

import { Container } from '@mui/material';

const Home = () => {
    return (
        <Container >
            <div id="app">
                <main>
                    <section id="about" className="anchor">
                        <div className="anchor-title">
                            <div>About</div>
                        </div>
                        <div className="col-container">
                            <div className="col">
                                <div className="about-title">Sharpen your skills</div>
                                <p className="about-content">Challenge yourself on questions, created by the community to strengthen
                                    different skills. Master
                                    your
                                    current language of
                                    choice, or expand your understanding of a new one.</p>
                            </div>

                            <div className="col">
                                <div className="about-title">Train on questions</div>
                                <p className="about-content">Solve the questions with your coding style right in the browser and use
                                    test cases (TDD) to check
                                    it
                                    as you progress. Retrain
                                    with new, creative, and optimized approaches.</p>
                            </div>
                            <div className="col">
                                <div className="about-title">Earn ranks and honor</div>
                                <p className="about-content">Questions are ranked to approximate difficulty. As you complete more
                                    questions, you progress
                                    through the ranks so we can
                                    match you with relevant challenges.</p>
                            </div>
                            <div className="col">
                                <div className="about-title">Gain collaborative wisdom</div>
                                <p className="about-content">Compare your solution with others after each questions for greater
                                    understanding. Discuss the
                                    questions, best practices, and
                                    innovative techniques with the community.</p>
                            </div>
                            <div className="col">
                                <div className="about-title">Create your own question</div>
                                <p className="about-content">Author questions that focus on your interests and train specific
                                    skillsets. Challenge the
                                    community with your insight and
                                    code understanding.</p>
                            </div>

                        </div>
                    </section>
                </main>
            </div>
        </Container>
    )
}

export default Home