import CategorySelectModal from '@/screens/modal/CategorySelectModal';
import Category from '@/utils/Category';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, TextInput, View} from 'react-native';
import {Button} from 'react-native-elements';

interface GroupEditCategoryBlockProps {
  navigation: any;
  subCategory: number[];
  setSubCategory: (idList: number[]) => void;
}

interface GroupEditCategoryBlockStates {
  modalVisible: boolean;
}

export default class GroupEditCategoryBlock extends React.Component<
  GroupEditCategoryBlockProps,
  GroupEditCategoryBlockStates
> {
  constructor(props: any) {
    super(props);
    this.state = {modalVisible: false};
  }

  render() {
    return (
      <View>
        <View style={this.styles.container}>
          <Text style={this.styles.title}>공통태그</Text>
          <Button
            buttonStyle={this.styles.button}
            titleStyle={this.styles.buttonTitle}
            onPress={() => this.setState({modalVisible: true})}
            title={'편집'}
          />
        </View>

        <View style={this.styles.textInputWrapper}>
          <TextInput
            style={this.styles.textInput}
            value={this.props.subCategory
              .map(id => `#${Category.getCategory(id)[1]}`)
              .join(' ')}
          />
        </View>
        <View>
          <CategorySelectModal
            modalVisible={this.state.modalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({modalVisible: visible});
            }}
            selectedCategoryList={this.props.subCategory.map(
              id => Category.getCategory(id)[1],
            )}
            setSubCategory={(sub: string[]) => {
              this.props.setSubCategory(
                sub.map(ss => Category.getIdWithSub(ss)),
              );
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    title: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginTop: 20,
      marginBottom: 10,
    },
    button: {
      width: 43,
      height: 28,
      borderRadius: 6,
      backgroundColor: 'transparent',
      paddingTop: 8,
      paddingBottom: 5,
    },
    buttonTitle: {
      color: '#4f6cff',
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
    },
    textInputWrapper: {
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      height: 44,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    textInput: {
      height: 44,
      width: '70%',
      paddingLeft: 13,
    },
  });
}
