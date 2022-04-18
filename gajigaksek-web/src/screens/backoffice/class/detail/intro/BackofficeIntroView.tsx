import React from "react";
import AdminLectureController from "../../../../../services/controllers/AdminLectureController";
import ClassAddNaviButton from "../../../../main/classAdd/ClassAddNaviButton";
import Cookies from "universal-cookie";

const cookies = new Cookies();

class ImgBlock {
  constructor() {
    this.src = this.text = this.title = "";
  }
  src: string;
  text: string;
  title: string;
}

interface BackofficeIntroViewStates {
  text: string;

  imgBlocks: ImgBlock[];
}

export default class BackofficeIntroView extends React.Component<
  Record<string, never>,
  BackofficeIntroViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      text: "",
      imgBlocks: [],
    };

    this.getDetail(+cookies.get("admin-lectureId"));
  }

  private async getDetail(lectureId: number) {
    try {
      const res = await AdminLectureController.getDetail(lectureId);
      const data = res?.data;
      this.setState({
        text: data.mainText,
        imgBlocks: data.finishedProductList.map((info: any) => {
          return {
            src: info.finishedProductImageUrl,
            text: info.text,
            title: info.finishedProductImageName,
          };
        }),
      });
    } catch (e) {}
  }

  render() {
    return (
      <div className="class-add-first-container">
        <div className="class-add-first-header">
          <button
            className="class-add-first-back"
            onClick={() => (window.location.href = "/backoffice/class")}
          >
            <i className="fas fa-chevron-left fa-3x"></i>
          </button>
          <div className="class-add-first-header-title pretendard">
            클래스 검수
          </div>
        </div>
        <div className="class-add-first-navigation">
          <ClassAddNaviButton on={false} title={"기본정보"} />
          <ClassAddNaviButton on title={"상세 소개"} />
          <ClassAddNaviButton on={false} title={"커리큘럼"} />
          <ClassAddNaviButton on={false} title={"스케줄"} />
          <ClassAddNaviButton on={false} title={"가격 및 쿠폰"} />
          <ClassAddNaviButton on={false} title={"부가정보"} />
        </div>
        <div className="class-add-first-main-container">
          <div className="class-add-first-name-container">
            <div className="class-add-mainCategory-title pretendard">
              클래스 소개
            </div>
            <div className="class-add-intro-input-wrapper">
              <textarea
                className="class-add-intro-input pretendard"
                placeholder="클래스 소개를 입력해주세요"
                value={this.state.text}
                onChange={
                  (event: any) => {
                    let dd;
                  }
                  // this.setState({ text: event.target.value })
                }
              ></textarea>
            </div>
          </div>

          <div className="class-add-mainCategory-title pretendard">
            완성작 소개
          </div>
          <div className="class-add-intro-img-container">
            {this.state.imgBlocks.map((imgBlock) => (
              <div className="class-add-intro-img-wrapper">
                <img
                  className={"class-add-first-img"}
                  src={imgBlock.src}
                  title={imgBlock.title}
                />
                <div className="class-add-intro-img-text-wrapper">
                  <input
                    className="class-add-intro-img-text pretendard"
                    placeholder="완성작 소개를 입력해주세요."
                    value={
                      this.state.imgBlocks.filter(
                        (img) => img.title === imgBlock.title
                      )[0].text
                    }
                    onChange={(event: any) => {
                      let dd;
                      // let imgBlocks = this.state.imgBlocks;

                      // imgBlocks = imgBlocks.map((img) => {
                      //   const temp = imgBlock;
                      //   if (img.title === temp.title) {
                      //     temp.text = event.target.value;
                      //     return temp;
                      //   }
                      //   return img;
                      // });
                      // this.setState({ imgBlocks });
                    }}
                  ></input>
                </div>
              </div>
            ))}
          </div>
        </div>
        <div className="class-add-first-bottom">
          <div>
            <button
              className="class-add-intro-bottom-left pretendard"
              onClick={() =>
                (window.location.href = "/backoffice/class-detail/first")
              }
            >
              이전
            </button>
            <button
              className="class-add-first-bottom-left pretendard"
              onClick={() =>
                (window.location.href = "/backoffice/class-detail/curriculum")
              }
            >
              다음
            </button>
          </div>
        </div>
      </div>
    );
  }
}
