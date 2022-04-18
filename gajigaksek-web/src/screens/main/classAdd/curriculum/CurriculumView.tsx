import React from "react";
import LectureController from "../../../../services/controllers/LectureController";
import { dataURLtoFile } from "../../../../utils/commonParams";
import ClassAddNaviButton from "../ClassAddNaviButton";
import CurriculumItemView from "./CurriculumItemView";
import "./CurriculumView.css";

export class CurriculumItem {
  constructor() {
    this.imgSrc = this.imgTitle = this.title = this.text = "";
  }

  imgSrc: string | undefined;
  imgTitle: string | undefined;
  title: string;
  text: string;
}

interface CurriculumViewStates {
  text: string;
  imgOn: boolean;
  curriculumTitle: string;
  curriculumItems: CurriculumItem[];
}

export default class CurriculumView extends React.Component<
  Record<string, never>,
  CurriculumViewStates
> {
  private inputRef;
  private imgRef;

  constructor(props: any) {
    super(props);
    this.state = {
      text: "",
      imgOn: false,
      curriculumTitle: "",
      curriculumItems: [],
    };
    this.inputRef = React.createRef<HTMLInputElement>();
    this.imgRef = React.createRef<HTMLImageElement>();
    this.getCurriculum();
  }

  private handleClick() {
    (this.inputRef.current as HTMLInputElement).click();
  }

  private handleFileUpload(e: any) {
    const files = e.target.files;
    if (!files || files.length <= 0) return;
    const selectedFile = files[0];
    const reader = new FileReader();

    const imgtag = this.imgRef.current as HTMLImageElement;
    imgtag.title = selectedFile.name;

    reader.onload = function (event) {
      if (event && event.target) imgtag.src = event.target.result as string;
    };

    this.setState({ imgOn: true });

    reader.readAsDataURL(selectedFile);
  }

  private addCurriculumItem(curriculumItem: CurriculumItem) {
    if (!curriculumItem.imgSrc || !curriculumItem.imgTitle) {
      alert("이미지를 다시 업로드해주세요.");
      return;
    }

    if (!curriculumItem.title) {
      alert("커리큘럼 제목을 입력해주세요.");
      return;
    }

    if (curriculumItem.title.length < 5 || curriculumItem.title.length >= 50) {
      alert("커리큘럼 제목은 5자 이상 50자 미만으로 입력해주세요.");
      return;
    }

    if (!curriculumItem.text) {
      alert("커리큘럼 세부내용을 입력해주세요.");
      return;
    }

    if (curriculumItem.text.length < 10 || curriculumItem.text.length >= 300) {
      alert("세부내용은 10자 이상 300자 미만으로 작성해주세요.");
      return;
    }

    this.setState({ imgOn: false, curriculumTitle: "", text: "" });
    if (this.imgRef)
      (this.imgRef.current as HTMLImageElement).title = (
        this.imgRef.current as HTMLImageElement
      ).src = "";

    this.setState({
      curriculumItems: [...this.state.curriculumItems, curriculumItem],
    });
  }

  private async putCurriculum(callback: () => void) {
    const params = {
      createLectureStep: "CURRICULUM",
      curriculumList: this.state.curriculumItems.map(
        (item: CurriculumItem, index: number) => {
          return {
            order: index + 1,
            title: item.title,
            detailText: item.text,
          };
        }
      ),
    };

    if (!this.checkForm(params)) {
      return;
    }

    const json = JSON.stringify(params);
    const blob = new Blob([json], {
      type: "application/json",
    });

    const formData = new FormData();
    formData.append("request", blob, "test1.json");

    const fileList = await Promise.all(
      this.state.curriculumItems.map(
        async (item: CurriculumItem, index: number) => {
          if (!item.imgSrc) return;
          const isUrl = item.imgSrc.slice(0, 5) === "https";

          if (isUrl) {
            const response = await fetch(
              "https://cors-anywhere.herokuapp.com/" + item.imgSrc,
              {
                headers: {
                  "x-requested-with": "*",
                },
              }
            );
            // here image is url/location of image
            const blob = await response.blob();
            return new File([blob], `image${index}.jpg`, { type: blob.type });
          } else {
            return dataURLtoFile(item.imgSrc, `tt${index}.jpeg`);
          }
        }
      )
    );
    fileList.forEach((file: any) => formData.append("files", file));

    try {
      await LectureController.putCurriculum(formData);
      callback();
    } catch (e) {}
  }

  private async getCurriculum() {
    try {
      const res = await LectureController.getCurriculum();
      const data = res?.data;
      this.setState({
        curriculumItems: data.curriculumList.map((info: any) => {
          return {
            imgSrc: info.curriculumImageUrl,
            imgTitle: info.curriculumImageName,
            title: info.title,
            text: info.detailText,
          };
        }),
      });
    } catch (e) {}
  }

  private checkForm(params: any) {
    if (!params.curriculumList || params.curriculumList.length <= 0) {
      alert("커리큘럼을 입력해주세요.");
      return false;
    }

    if (params.curriculumList.length > 4) {
      alert("커리큘럼은 최소1개 최대4개 입력 가능합니다.");
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
          <ClassAddNaviButton on title={"커리큘럼"} />
          <ClassAddNaviButton on={false} title={"스케줄"} />
          <ClassAddNaviButton on={false} title={"가격 및 쿠폰"} />
          <ClassAddNaviButton on={false} title={"부가정보"} />
        </div>
        <div className="class-add-first-main-container">
          <div className="class-add-first-name-container">
            <div className="class-add-mainCategory-title pretendard">
              커리큘럼 상세등록
            </div>
            <div className="class-add-curriculum-form-container">
              <div className="class-add-curriculum-form-img-wrapper">
                <div className="class-add-curriculum-form-img-title pretendard">
                  사진 등록
                </div>
                <div className="class-add-curriculum-form-img-wrapper">
                  <button
                    className="class-add-curriculum-form-img-button"
                    onClick={() => this.handleClick()}
                  >
                    {!this.state.imgOn && (
                      <div>
                        <i className="fas fa-plus fa-2x"></i>

                        <div className="class-add-curriculum-form-img-button-text pretendard">
                          사진 등록
                        </div>
                      </div>
                    )}
                    <input
                      type="file"
                      className="class-add-curriculum-img-input"
                      accept="image/png, image/jpeg"
                      onChange={(e: any) => this.handleFileUpload(e)}
                      ref={this.inputRef}
                    ></input>

                    <img
                      className={`class-add-curriculum-img ${
                        this.state.imgOn ? "" : "display-none"
                      }`}
                      ref={this.imgRef}
                      src=""
                    />
                  </button>
                </div>
              </div>
              <div className="class-add-curriculum-info-input-wrapper">
                <div className="class-add-curriculum-info-input-one">
                  <div className="class-add-curriculum-form-img-title pretendard">
                    구분 선택
                  </div>
                  <div className="class-add-curriculum-info-input-small prtendard">
                    구분을 선택해주세요
                  </div>
                  <select className="class-add-curriculum-info-select pretendard">
                    <option value="전체">전체</option>
                  </select>
                </div>
                <div className="class-add-curriculum-info-input-two">
                  <div className="class-add-curriculum-form-img-title pretendard">
                    제목
                  </div>
                  <div className="class-add-curriculum-info-input-small prtendard">
                    수업 순서의 제목을 입력해주세요. ex) 재료소개, 도안고르기 등
                  </div>
                  <div className="class-add-curriculum-info-input-two-input-wrapper">
                    <input
                      className="class-add-first-name-input pretendard"
                      placeholder="제목을 입력해주세요."
                      value={this.state.curriculumTitle}
                      onChange={(event: any) =>
                        this.setState({ curriculumTitle: event.target.value })
                      }
                    ></input>
                  </div>
                </div>
              </div>
              <div className="class-add-curriculum-detail-wrapper">
                <div className="class-add-curriculum-form-img-title pretendard">
                  세부 내용
                </div>
                <div className="class-add-curriculum-info-input-small prtendard">
                  세부내용을 입력해주세요.
                </div>
                <div className="class-add-curriculum-info-input-small prtendard">
                  ex) 사용하는 재료에 대해 소개하고 알아본다.
                </div>
                <div className="class-add-curriculum-detail-input-wrapper">
                  <textarea
                    className="class-add-curriculum-textarea pretendard"
                    placeholder="세부내용을 입력해주세요."
                    value={this.state.text}
                    onChange={(event: any) =>
                      this.setState({ text: event.target.value })
                    }
                  ></textarea>
                </div>
              </div>
            </div>
            <button
              className="class-add-first-bottom-left pretendard asdf"
              onClick={() => {
                const curriculumItem = new CurriculumItem();
                curriculumItem.imgSrc = this.imgRef.current?.src;
                curriculumItem.imgTitle = this.imgRef.current?.title;
                curriculumItem.title = this.state.curriculumTitle;
                curriculumItem.text = this.state.text;

                this.addCurriculumItem(curriculumItem);
              }}
            >
              <i className="fas fa-plus fa asdf2"></i>
              입력
            </button>
          </div>
        </div>

        <div className="class-add-curriculum-table-wrapper">
          <div className="class-add-curriculum-table-header">
            <div className="sos11 class-add-curriculum-table-header1 pretendard">
              순서
            </div>
            <div className="sos11 class-add-curriculum-table-header2 pretendard">
              사진
            </div>
            <div className="sos11 class-add-curriculum-table-header3 pretendard">
              제목
            </div>
            <div className="sos11 class-add-curriculum-table-header4 pretendard">
              내용
            </div>
          </div>
          {this.state.curriculumItems.map((item: CurriculumItem) => (
            <CurriculumItemView
              onClickUp={() => {
                const items = this.state.curriculumItems;
                const idx = items.indexOf(item);
                if (idx <= 0) return;

                const temp = items[idx];
                items[idx] = items[idx - 1];
                items[idx - 1] = temp;

                this.setState({ curriculumItems: items });
              }}
              onClickDown={() => {
                const items = this.state.curriculumItems;
                const idx = items.indexOf(item);
                if (idx >= items.length - 1) return;

                const temp = items[idx];
                items[idx] = items[idx + 1];
                items[idx + 1] = temp;

                this.setState({ curriculumItems: items });
              }}
              item={item}
            />
          ))}
        </div>
        <div className="class-add-first-bottom">
          <div>
            <button
              className="class-add-intro-bottom-left pretendard"
              onClick={() => (window.location.href = "/main/add/class/intro")}
            >
              이전
            </button>
            <button
              className="class-add-first-bottom-left pretendard"
              onClick={() =>
                this.putCurriculum(
                  () => (window.location.href = "/main/add/class/schedule")
                )
              }
            >
              다음
            </button>
          </div>
          <button
            className="class-add-first-bottom-right pretendard"
            onClick={() => this.putCurriculum(() => alert("저장되었습니다."))}
          >
            저장
          </button>
        </div>
      </div>
    );
  }
}
