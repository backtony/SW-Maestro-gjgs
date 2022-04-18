import React from 'react';
import {StyleSheet, View} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';

interface LectureDetailViewTeamSelectProps {
  applyTeamDropDown: boolean;
  selectedGroup: number;
  teamList: any;
  setApplyTeamDropDown: (applyTeamDropDown: boolean) => void;
  setTeam: (callback: any) => void;
}

export default class LectureDetailViewTeamSelect extends React.Component<
  LectureDetailViewTeamSelectProps,
  {}
> {
  render() {
    return (
      <View style={{marginHorizontal: 20, marginTop: 20}}>
        <DropDownPicker
          open={this.props.applyTeamDropDown}
          value={this.props.selectedGroup}
          items={[
            {
              label: '그룹을 선택하세요.',
              value: 0,
            },
            ...this.props.teamList.map(item => ({
              label: item.teamName,
              value: item.teamId,
            })),
          ]}
          setOpen={() => {
            this.props.setApplyTeamDropDown(!this.props.applyTeamDropDown);
          }}
          dropDownDirection="TOP"
          setValue={callback => {
            this.props.setTeam(callback);
          }}
          setItems={() => {}}
          showTickIcon={false}
          style={this.styles.dropDown}
          listItemContainerStyle={{height: 28}}
          listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}} //dd
          dropDownContainerStyle={{
            borderWidth: 0,
          }}
          arrowIconStyle={{
            width: 18,
            height: 18,
          }}
          textStyle={{fontSize: 14, fontWeight: 'bold'}}
        />
      </View>
    );
  }

  private styles = StyleSheet.create({
    dropDown: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
      flex: 1,
    },
  });
}
