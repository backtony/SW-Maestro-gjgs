import Category from '@/utils/Category';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, TextInput, View} from 'react-native';
import {Button} from 'react-native-elements';

interface GroupCategoryBlockProps {
  subCategory: number[];
  categoryModalVisible: boolean;
  setCategoryModalVisible: (categoryModalVisible: boolean) => void;
}

export default class GroupCategoryBlock extends React.Component<
  GroupCategoryBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>공통태그</Text>
        <View style={this.styles.container}>
          <TextInput
            style={this.styles.textInput}
            value={this.props.subCategory
              .map(id => `#${Category.getCategory(id)[1]}`)
              .join(' ')}
          />
          <Button
            buttonStyle={this.styles.button}
            titleStyle={this.styles.buttonTitle}
            onPress={() => {
              this.props.setCategoryModalVisible(
                !this.props.categoryModalVisible,
              );
            }}
            title={'변경'}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    text: {
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
      marginTop: 20,
      marginBottom: 10,
    },
    container: {
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
    button: {
      width: 43,
      height: 28,
      borderRadius: 6,
      backgroundColor: '#4f6cff',
      marginRight: 13,
      paddingTop: 8,
      paddingBottom: 5,
    },
    buttonTitle: {
      color: '#FFF',
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
    },
  });
}
