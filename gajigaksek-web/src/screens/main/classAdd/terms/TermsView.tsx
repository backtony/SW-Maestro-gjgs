import React from "react";
import LectureController from "../../../../services/controllers/LectureController";
import ClassAddNaviButton from "../ClassAddNaviButton";
import TermsBlock from "./TermsBlock";

interface TermsViewStates {
  termsOne: boolean;
  termsTwo: boolean;
  termsThree: boolean;
  termsFour: boolean;
}

const termsText = [
  `위하여 돋고, 할지라도 발휘하기 원대하고, 커다란 듣는다. 뜨거운지라, 품었기 노년에게서 살 뿐이다. 남는 보이는 수 장식하는 귀는 이는 소금이라 있는가? 못할 피부가 봄바람을 하였으며, 생생하며, 있다. 끝에 인간의 그들의 밥을 가치를 이것은 꽃이 붙잡아 피어나는 것이다. 찾아 얼마나 불어 것은 황금시대의 있는가? 용기가 이 위하여, 가치를 있으랴? 이상 그들에게 무엇을 밥을 수 속에서 설레는 얼음과 봄바람이다. 위하여서, 소금이라 보배를 간에 기관과 착목한는 원질이 풍부하게 실로 사막이다. 현저하게 산야에 이상의 곧 싹이 피다. 아름답고 이상은 수 그것은 가진 장식하는 생의 보라. 자신과 싹이 이상, 놀이 크고 가치를 보라. 방황하여도, 힘차게 못하다 꽃이 열매를 만물은 곳이 칼이다. 가지에 꽃이 우리 든 구하지 인간의 트고, 날카로우나 말이다. 이상의 대고, 능히 대한 못하다 사막이다. 피고, 인간에 못할 풍부하게 이상이 것이다. 이상은 행복스럽고 과실이 더운지라 구하지 뭇 것이다. 되는 있을 쓸쓸한 사랑의 인생에 얼음 그들의 따뜻한 운다. 같은 뭇 밝은 노래하며 곳으로 평화스러운 것이다. 이것은 뼈 예가 우리 것이다.
  황금시대의 살았으며, 피고, 이는 남는 힘차게 있는 그리하였는가? 이상의 이상은 끓는 사막이다. 같으며, 아니더면, 귀는 피고, 인생에 이상, 힘차게 피에 같이, 사막이다. 같지 것은 있는 눈이 고동을 무엇을 두기 끓는다. 그것을 가치를 무한한 뛰노는 그것은 끓는다. 것이다.보라, 원대하고, 이상의 착목한는 듣기만 시들어 그러므로 이상이 예가 듣는다. 위하여서 천고에 이것은 거친 밝은 있으며, 두기 그들은 것이다. 없으면 우리의 가치를 인생에 곧 그들은 가슴에 아름다우냐? 미묘한 그러므로 그러므로 전인 창공에 것은 무엇을 말이다. 넣는 불어 피고, 밝은 얼마나 사랑의 같지 길지 있는가? 아니한 구하지 갑 있는 얼마나 황금시대다.`,
];

export default class TermsView extends React.Component<
  Record<string, never>,
  TermsViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      termsOne: false,
      termsTwo: false,
      termsThree: false,
      termsFour: false,
    };
  }

  private async postTerms(callback: () => void) {
    const params = {
      createLectureStep: "TERMS",
      termsOne: this.state.termsOne,
      termsTwo: this.state.termsTwo,
      termsThree: this.state.termsThree,
      termsFour: this.state.termsFour,
    };

    if (!this.checkForm(params)) return;

    try {
      await LectureController.postTerms(params);
      callback();
    } catch (e) {}
  }

  private checkForm(params: any) {
    if (!params.termsOne) {
      alert("약관1에 동의해주세요.");
      return false;
    }
    if (!params.termsTwo) {
      alert("약관2에 동의해주세요.");
      return false;
    }
    if (!params.termsThree) {
      alert("약관3에 동의해주세요.");
      return false;
    }
    if (!params.termsFour) {
      alert("약관4에 동의해주세요.");
      return false;
    }
    return true;
  }

  render() {
    return (
      <div className="class-add-first-container">
        <div className="class-add-first-header">
          <button
            className="class-add-first-back"
            onClick={() => (window.location.href = "/main/home")}
          >
            <i className="fas fa-chevron-left fa-3x"></i>
          </button>
          <div className="class-add-first-header-title pretendard">
            클래스 등록
          </div>
        </div>
        <div className="class-add-first-navigation">
          <ClassAddNaviButton on={false} title={"기본정보"} />
          <ClassAddNaviButton on={false} title={"상세 소개"} />
          <ClassAddNaviButton on={false} title={"커리큘럼"} />
          <ClassAddNaviButton on={false} title={"스케줄"} />
          <ClassAddNaviButton on={false} title={"가격 및 쿠폰"} />
          <ClassAddNaviButton on title={"부가정보"} />
        </div>
        <div className="class-add-first-main-container">
          <TermsBlock
            title={"이용 약관 동의"}
            text={termsText[0]}
            checked={this.state.termsOne}
            onClick={(checked: boolean) => this.setState({ termsOne: checked })}
          />
          <TermsBlock
            title={"개인 정보 수집 및 이용 동의"}
            text={termsText[0]}
            checked={this.state.termsTwo}
            onClick={(checked: boolean) => this.setState({ termsTwo: checked })}
          />
          <TermsBlock
            title={"제 3자 정보 제공 동의"}
            text={termsText[0]}
            checked={this.state.termsThree}
            onClick={(checked: boolean) =>
              this.setState({ termsThree: checked })
            }
          />
          <TermsBlock
            title={"이용 약관 동의2"}
            text={termsText[0]}
            checked={this.state.termsFour}
            onClick={(checked: boolean) =>
              this.setState({ termsFour: checked })
            }
          />
        </div>
        <div className="class-add-first-bottom">
          <div>
            <button
              className="class-add-intro-bottom-left pretendard"
              onClick={() =>
                (window.location.href = "/main/add/class/price-coupon")
              }
            >
              이전
            </button>
            <button
              className="class-add-first-bottom-left pretendard"
              onClick={() =>
                this.postTerms(() => {
                  window.localStorage.removeItem("lectureId");
                  alert("클래스 생성이 완료되었습니다.");
                  window.location.href = "/main/home";
                })
              }
            >
              완료
            </button>
          </div>
        </div>
      </div>
    );
  }
}
