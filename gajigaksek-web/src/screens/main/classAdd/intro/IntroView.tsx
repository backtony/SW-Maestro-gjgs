import React from "react";
import LectureController from "../../../../services/controllers/LectureController";
import { dataURLtoFile } from "../../../../utils/commonParams";
import ClassAddNaviButton from "../ClassAddNaviButton";
import "./IntroView.css";

class ImgBlock {
  constructor() {
    this.src = this.text = this.title = "";
  }
  src: string;
  text: string;
  title: string;
}

interface IntroViewStates {
  text: string;

  imgBlocks: ImgBlock[];
}

export default class IntroView extends React.Component<
  Record<string, never>,
  IntroViewStates
> {
  private inputRef;
  private uploading = false;

  constructor(props: any) {
    super(props);
    this.state = {
      text: "",
      imgBlocks: [],
    };
    this.inputRef = React.createRef<HTMLInputElement>();

    this.getIntro();
  }

  private handleClick() {
    (this.inputRef.current as HTMLInputElement).click();
  }

  private handleFileUpload(e: any) {
    const files = e.target.files;
    if (!files || files.length <= 0) return;

    const selectedFile = files[0];
    const reader = new FileReader();

    const imgBlock: ImgBlock = new ImgBlock();
    imgBlock.title = `${Date.now()}${selectedFile.name}`;

    const addImgBlock = this.addImgBlock.bind(this);

    reader.onload = function (event) {
      if (event && event.target) {
        imgBlock.src = event.target.result as string;
        e.target.value = "";
        addImgBlock(imgBlock);
      }
    };

    reader.readAsDataURL(selectedFile);
  }

  private addImgBlock(imgBlock: ImgBlock) {
    this.setState({ imgBlocks: this.state.imgBlocks.concat(imgBlock) });
  }

  private async putIntro(callback: () => void) {
    const params = {
      createLectureStep: "INTRO",
      mainText: this.state.text,
      finishedProductInfoList: this.state.imgBlocks.map(
        (imgBlock: ImgBlock, index: number) => {
          return {
            order: index + 1,
            text: imgBlock.text,
          };
        }
      ),
    };

    if (!this.checkForm(params)) return;

    const json = JSON.stringify(params);
    const blob = new Blob([json], {
      type: "application/json",
    });

    const formData = new FormData();
    formData.append("request", blob, "test1.json");

    const fileList = await Promise.all(
      this.state.imgBlocks.map(async (imgBlock: ImgBlock, index: number) => {
        const isUrl = imgBlock.src.slice(0, 5) === "https";

        if (isUrl) {
          const response = await fetch(
            "https://cors-anywhere.herokuapp.com/" + imgBlock.src,
            {
              headers: {
                "x-requested-with": "*",
                "Access-Control-Allow-Origin": "*",
              },
            }
          );
          // here image is url/location of image
          const blob = await response.blob();
          return new File([blob], `image${index}.jpeg`, { type: blob.type });
        } else {
          return dataURLtoFile(imgBlock.src, `tt${index}.jpeg`);
        }
      })
    );
    fileList.forEach((file: any) => formData.append("files", file));

    try {
      await LectureController.putIntro(formData);
      callback();
    } catch (e) {}
  }

  private checkForm(params: any) {
    if (!params.mainText) {
      alert("소개를 입력해주세요.");
      return false;
    }

    if (params.mainText.length < 10 || params.mainText.length >= 500) {
      alert("10자이상 500자 미만으로 입력해주세요.");
      return false;
    }

    if (
      !params.finishedProductInfoList ||
      params.finishedProductInfoList.length <= 0
    ) {
      alert("완성작 사진을 추가해주세요.");
      return false;
    }

    if (params.finishedProductInfoList.length > 4) {
      alert("완성작 사진은 1장 이상 4장 이하로 올려주세요.");
      return false;
    }

    //완성작 텍스트 확인
    let flag = false;

    params.finishedProductInfoList.forEach((info: ImgBlock) => {
      if (!info.text) {
        flag = true;
      }
    });

    if (flag) {
      alert("완성작 설명을 입력해주세요.");
      return false;
    }

    return true;
  }

  private async getIntro() {
    try {
      const res = await LectureController.getIntro();
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
                className="class-add-intro-input pretendard saramsalrye"
                placeholder="클래스 소개를 입력해주세요"
                value={this.state.text}
                onChange={(event: any) =>
                  this.setState({ text: event.target.value })
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
                      let imgBlocks = this.state.imgBlocks;

                      imgBlocks = imgBlocks.map((img) => {
                        const temp = imgBlock;
                        if (img.title === temp.title) {
                          temp.text = event.target.value;
                          return temp;
                        }
                        return img;
                      });
                      this.setState({ imgBlocks });
                    }}
                  ></input>
                </div>
              </div>
            ))}

            <div className="class-add-first-img-wrapper">
              <button
                className="class-add-first-img-button"
                onClick={() => this.handleClick()}
              >
                <div>
                  <i className="fas fa-plus fa-2x"></i>

                  <div className="class-add-first-img-button-text pretendard">
                    이미지 추가
                  </div>
                </div>
                <input
                  type="file"
                  className="class-add-first-img-input"
                  accept="image/png, image/jpeg"
                  ref={this.inputRef}
                  onChange={(e: any) => this.handleFileUpload(e)}
                ></input>
              </button>
            </div>
          </div>
        </div>
        <div className="class-add-first-bottom">
          <div>
            <button
              className="class-add-intro-bottom-left pretendard"
              onClick={() => (window.location.href = "/main/add/class/first")}
            >
              이전
            </button>
            <button
              className="class-add-first-bottom-left pretendard"
              onClick={() =>
                this.putIntro(
                  () => (window.location.href = "/main/add/class/curriculum")
                )
              }
            >
              다음
            </button>
          </div>
          <button
            className="class-add-first-bottom-right pretendard"
            onClick={() => {
              this.putIntro(() => alert("저장이 완료되었습니다."));
            }}
          >
            저장
          </button>
        </div>
      </div>
    );
  }
}
