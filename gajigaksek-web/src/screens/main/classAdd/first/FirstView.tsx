import React from "react";
import ClassAddNaviButton from "../ClassAddNaviButton";
import "./FirstView.css";
import ClassAddGeneralButton from "../ClassAddGeneralButton";
import Category from "../../../../utils/Category";
import LectureController from "../../../../services/controllers/LectureController";

interface FirstViewStates {
  mainCategory: number;
  subCategory: number;
  title: string;
  imgOn: boolean;
  address: string;
}

export default class FirstView extends React.Component<
  Record<string, never>,
  FirstViewStates
> {
  private inputRef;
  private imgRef;

  constructor(props: any) {
    super(props);
    this.state = {
      mainCategory: 1,
      subCategory: 2,
      title: "",
      imgOn: false,
      address: "",
    };
    this.inputRef = React.createRef<HTMLInputElement>();
    this.imgRef = React.createRef<HTMLImageElement>();
    this.getFirst();
  }

  private handleClick() {
    (this.inputRef.current as HTMLInputElement).addEventListener(
      "change",
      (e: any) => this.handleFileUpload(e)
    );
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

  private checkForm(params: any, files: FileList | null | undefined) {
    if (!params.categoryId) {
      alert("소분류를 선택해주세요.");
      return false;
    }

    if (!params.zoneId) {
      alert("지역을 선택해주세요.");
      return false;
    }

    if (!params.title) {
      alert("제목을 입력해주세요.");
      return false;
    }

    if (params.title.length < 10 || params.title.length >= 100) {
      alert("제목은 10자 이상 100자 이하로 입력해주세요.");
      return false;
    }

    if (!params.address) {
      alert("주소를 입력해주세요.");
      return false;
    }

    if (!files) {
      alert("이미지를 추가해주세요.");
      return false;
    }

    return true;
  }

  private async putFirst(callback: () => void) {
    const params = {
      createLectureStep: "FIRST",
      categoryId: this.state.subCategory,
      zoneId: 2,
      title: this.state.title,
      address: this.state.address,
    };

    const imageFile = this.inputRef.current?.files;
    if ((!imageFile || imageFile.length <= 0) && this.imgRef.current) {
      const response = await fetch(
        "https://cors-anywhere.herokuapp.com/" + this.imgRef.current.src,
        {
          headers: {
            "x-requested-with": "*",
          },
        }
      );
      // here image is url/location of image
      const blob = await response.blob();
      const file = new File([blob], "image.jpg", { type: blob.type });

      const json = JSON.stringify(params);
      const blob1 = new Blob([json], {
        type: "application/json",
      });

      const formData = new FormData();
      formData.append("request", blob1, "test1.json");
      formData.append("files", file);
      console.log("sklsklsklskl: ", params);
      try {
        const res = await LectureController.putFirst(formData);
        callback();
      } catch (e) {}
    } else {
      if (!this.checkForm(params, imageFile)) return;
      if (!imageFile) return;

      const json = JSON.stringify(params);
      const blob = new Blob([json], {
        type: "application/json",
      });

      const formData = new FormData();
      formData.append("request", blob, "test1.json");
      formData.append("files", imageFile[0]);
      console.log("sklsklsklskl: ", params);
      try {
        await LectureController.putFirst(formData);
        callback();
      } catch (e) {}
    }
  }

  private async getFirst() {
    try {
      const res = await LectureController.getFirst();
      const data = res?.data;
      this.setState({
        mainCategory: Category.getMainId(data.categoryId),
        subCategory: data.categoryId,
        title: data.title,
        address: data.address,
      });
      if (this.imgRef.current) {
        this.imgRef.current.src = data.thumbnailImageFileUrl;
        this.setState({ imgOn: true });
      }
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
          <ClassAddNaviButton on title={"기본정보"} />
          <ClassAddNaviButton on={false} title={"상세 소개"} />
          <ClassAddNaviButton on={false} title={"커리큘럼"} />
          <ClassAddNaviButton on={false} title={"스케줄"} />
          <ClassAddNaviButton on={false} title={"가격 및 쿠폰"} />
          <ClassAddNaviButton on={false} title={"부가정보"} />
        </div>
        <div className="class-add-first-main-container">
          <div className="class-add-mainCategory-container">
            <div className="class-add-mainCategory-title pretendard">
              대분류
            </div>
            <div className="class-add-mainCategory-button-container">
              {Category.getCategoryList().map((big) => (
                <ClassAddGeneralButton
                  title={big}
                  onClick={() =>
                    this.setState({ mainCategory: Category.getId(big, "") })
                  }
                  on={this.state.mainCategory === Category.getId(big, "")}
                />
              ))}
            </div>
          </div>
          <div className="class-add-subCategory-container">
            <div className="class-add-mainCategory-title pretendard">
              소분류
            </div>
            <div className="class-add-mainCategory-button-container">
              {Category.getSubCategoryList(
                Category.getCategory(this.state.mainCategory)[0]
              ).map((sub) => (
                <ClassAddGeneralButton
                  title={sub.sub}
                  onClick={() => this.setState({ subCategory: +sub.id })}
                  on={this.state.subCategory === +sub.id}
                />
              ))}
            </div>
          </div>

          <div className="class-add-first-name-container">
            <div className="class-add-mainCategory-title pretendard">
              클래스 제목
            </div>
            <div className="class-add-first-name-wrapper">
              <input
                className="class-add-first-name-input pretendard"
                placeholder="클래스 제목"
                value={this.state.title}
                onChange={(event: any) =>
                  this.setState({ title: event.target.value })
                }
              ></input>
            </div>
          </div>

          <div className="class-add-first-img-container">
            <div className="class-add-mainCategory-title pretendard">
              대표 사진
            </div>
            <div className="class-add-first-img-wrapper">
              <button
                className="class-add-first-img-button"
                onClick={() => this.handleClick()}
              >
                {!this.state.imgOn && (
                  <div>
                    <i className="fas fa-plus fa-2x"></i>

                    <div className="class-add-first-img-button-text pretendard">
                      이미지 추가
                    </div>
                  </div>
                )}
                <input
                  type="file"
                  className="class-add-first-img-input"
                  accept="image/png, image/jpeg"
                  ref={this.inputRef}
                ></input>

                <img
                  className={`class-add-first-img ${
                    this.state.imgOn ? "" : "display-none"
                  }`}
                  ref={this.imgRef}
                  src=""
                />
              </button>
            </div>
          </div>

          <div className="class-add-first-address-container">
            <div className="class-add-mainCategory-title pretendard">주소</div>
            <div className="class-add-first-name-wrapper">
              <input
                className="class-add-first-name-input pretendard"
                placeholder="주소 입력"
                value={this.state.address}
                onChange={(event: any) =>
                  this.setState({ address: event.target.value })
                }
              ></input>
            </div>
          </div>
        </div>
        <div className="class-add-first-bottom">
          <button
            className="class-add-first-bottom-left pretendard"
            onClick={() => {
              this.putFirst(() => {
                window.location.href = "/main/add/class/intro";
              });
            }}
          >
            다음
          </button>
          <button
            className="class-add-first-bottom-right pretendard"
            onClick={() => this.putFirst(() => alert("저장되었습니다."))}
          >
            저장
          </button>
        </div>
      </div>
    );
  }
}
