package com.gjgs.gjgs.dummy.lecture;

import com.gjgs.gjgs.modules.utils.vo.FileInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class FileUrlDummy {

    public List<FileInfoVo> getBoardFinishedProductFileUrlDummy() {
        return List.of(
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1466074395296-41cba23ce4f8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1068&q=80").fileName("finishedProductInfoFile1").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1589542425426-2460d8243b58?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=975&q=80").fileName("finishedProductInfoFile2").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1531565637446-32307b194362?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=926&q=80").fileName("finishedProductInfoFile3").build()
        );
    }

    public List<FileInfoVo> getBoardCurriculumFileUrlDummy() {
        return List.of(
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1589712186148-03ec318289c0?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80").fileName("finishedProductInfoFile1").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1599501887769-a945a7e4fece?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80").fileName("finishedProductInfoFile2").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1559054072-34d9969e75ca?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=934&q=80").fileName("finishedProductInfoFile3").build()
        );
    }

    public List<FileInfoVo> getClimbingFinishedProductFileUrlDummy() {
        return List.of(
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1539755848297-488081ec9829?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=934&q=80").fileName("finishedProductInfoFile1").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1599499654517-21fce9a3186f?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=933&q=80").fileName("finishedProductInfoFile2").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1527618956224-702fcb42217b?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=934&q=80").fileName("finishedProductInfoFile3").build()
        );
    }

    public List<FileInfoVo> getClimbingCurriculumFileUrlDummy() {
        return List.of(
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1610768798760-e01a4b43814e?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80").fileName("finishedProductInfoFile1").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1605548109944-9040d0972bf5?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1050&q=80").fileName("finishedProductInfoFile2").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1595931285307-ec7ab5b0a1f7?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80").fileName("finishedProductInfoFile3").build()
        );
    }

    public List<FileInfoVo> getCookingFinishedProductFileUrlDummy() {
        return List.of(
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1584278858536-52532423b9ea?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80").fileName("finishedProductInfoFile1").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1628441309764-794e7362f6e6?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80").fileName("finishedProductInfoFile2").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1600289031464-74d374b64991?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=969&q=80").fileName("finishedProductInfoFile3").build()
        );
    }

    public List<FileInfoVo> getCookingCurriculumFileUrlDummy() {
        return List.of(
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1606787620819-8bdf0c44c293?ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80").fileName("finishedProductInfoFile1").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1528712306091-ed0763094c98?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=980&q=80").fileName("finishedProductInfoFile2").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1605522561233-768ad7a8fabf?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1052&q=80").fileName("finishedProductInfoFile3").build()
        );
    }

    public List<FileInfoVo> getBaristaFinishedProductFileUrlDummy() {
        return List.of(
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1509042239860-f550ce710b93?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80").fileName("finishedProductInfoFile1").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1511920170033-f8396924c348?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1234&q=80").fileName("finishedProductInfoFile2").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1485808191679-5f86510681a2?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=934&q=80").fileName("finishedProductInfoFile3").build()
        );
    }

    public List<FileInfoVo> getBaristaCurriculumFileUrlDummy() {
        return List.of(
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1597018990612-969bb248a215?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1000&q=80").fileName("finishedProductInfoFile1").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1547983896-a51f56785650?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80").fileName("finishedProductInfoFile2").build(),
                FileInfoVo.builder().fileUrl("https://images.unsplash.com/photo-1599000117646-493e27e0e609?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80").fileName("finishedProductInfoFile3").build()
        );
    }
}
